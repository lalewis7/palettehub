package net.palettehub.api.palette;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.palettehub.api.AppTest;
import net.palettehub.api.MySQLContainerBaseTest;
import net.palettehub.api.jwt.JwtUtil;
import net.palettehub.api.palette.exception.PageValueInvalidException;
import net.palettehub.api.palette.exception.Palette404Exception;
import net.palettehub.api.palette.exception.PaletteLikeException;
import net.palettehub.api.palette.exception.SortValueInvalidException;
import net.palettehub.api.user.User;
import net.palettehub.api.user.UserTest;
import net.palettehub.exception.RestrictedAccessException;

@RunWith(SpringJUnit4ClassRunner.class)
public class PaletteServiceTest extends MySQLContainerBaseTest{
    
    @Autowired
    private PaletteService paletteService;

    @Autowired
    private JwtUtil jwtUtil;

    @After
    public void deleteRows() throws SQLException{
        AppTest.deleteAllData(dataSource.getConnection());
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void injectedComponentsAreNotNull() {
        assertNotEquals(null, dataSource);
        assertNotEquals(null, paletteService);
    }

    @Test
    public void checkGetPalettesPopular() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        for (int i = 0; i < users.length; i++){
            PreparedStatement ps = UserTest.insertUserPreparedStatement(dataSource.getConnection(), users[i]);
            assertEquals(1, ps.executeUpdate());
        }

        Palette[] palettes = PaletteTest.getSamplePalettes(3);
        for (int i = 0; i < palettes.length; i++){
            palettes[i].setPosted(100000 - 1000*i);
            palettes[i].setUserId(users[0].getUserId());
            PreparedStatement ps2 = PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palettes[i]);
            assertEquals(1, ps2.executeUpdate());
        }

        for (int p = 0; p < palettes.length; p++){
            for (int u = 0; u < users.length - (palettes.length - p - 1); u++){
                PreparedStatement ps = PaletteTest.insertLikePreparedStatement(dataSource.getConnection(), users[u].getUserId(), palettes[p].getPaletteId());
                assertEquals(1, ps.executeUpdate());
            }
        }
        
        PaletteList pl = paletteService.getPalettes(users[0].getUserId(), "popular", "1");
        assertEquals(3, pl.getCount());
        List<Palette> testPalettes = pl.getPalettes();
        for (int i = 0; i < testPalettes.size(); i++){
            assertEquals(palettes[palettes.length-i-1].getPaletteId(), testPalettes.get(i).getPaletteId());
        }
    }

    @Test
    public void checkGetPaletteNew() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        PreparedStatement ps = UserTest.insertUserPreparedStatement(dataSource.getConnection(), user);
        assertEquals(1, ps.executeUpdate());

        Palette[] palettes = PaletteTest.getSamplePalettes(3);
        for (int i = 0; i < palettes.length; i++){
            palettes[i].setPosted(100000 - 1000*i);
            palettes[i].setUserId(user.getUserId());
            PreparedStatement ps2 = PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palettes[i]);
            assertEquals(1, ps2.executeUpdate());
        }

        PaletteList paletteList = paletteService.getPalettes(user.getUserId(), "new", "1");
        assertEquals(3, paletteList.getCount());
        List<Palette> testPalettes = paletteList.getPalettes();
        for (int i = 0; i < testPalettes.size(); i++){
            assertEquals(palettes[i].getPaletteId(), testPalettes.get(i).getPaletteId());
        }
    }

    @Test
    public void checkGetPalettesInvalidSort() throws SQLException{
        assertThrows(SortValueInvalidException.class, () -> {paletteService.getPalettes("", "dwaosda", "1");});
    }

    @Test
    public void checkGetPalettesInvalidPage() throws SQLException{
        assertThrows(PageValueInvalidException.class, () -> {paletteService.getPalettes("", "new", "a");});
    }

    @Test
    public void checkCreatePalette() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        String paletteId = paletteService.createPalette(user.getUserId(), palette);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM palettes WHERE palette_id = ?");
        ps.setString(1, paletteId);
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(res.getString("user_id"), palette.getUserId());
        assertEquals(res.getString("color_1"), palette.getColor1());
        assertEquals(res.getString("color_2"), palette.getColor2());
        assertEquals(res.getString("color_3"), palette.getColor3());
        assertEquals(res.getString("color_4"), palette.getColor4());
        assertEquals(res.getString("color_5"), palette.getColor5());
    }

    @Test
    public void checkGetPalette() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        Palette pal = paletteService.getPalette(user.getUserId(), palette.getPaletteId());

        assertEquals(palette.getColor1(), pal.getColor1());
        assertEquals(palette.getColor2(), pal.getColor2());
        assertEquals(palette.getColor3(), pal.getColor3());
        assertEquals(palette.getColor4(), pal.getColor4());
        assertEquals(palette.getColor5(), pal.getColor5());
    }

    @Test
    public void checkGetPaletteInvalidId() throws SQLException{
        assertThrows(Palette404Exception.class, () -> {paletteService.getPalette("", "123");});
    }

    @Test
    public void checkGetPaletteWrongId() throws SQLException{
        assertThrows(Palette404Exception.class, () -> {paletteService.getPalette("", AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS));});
    }
    
    @Test
    public void checkLikePalette() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        paletteService.likePalette(user.getUserId(), palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM likes WHERE user_id = ? AND palette_id = ?");
        ps.setString(1, user.getUserId());
        ps.setString(2, palette.getPaletteId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
    }

    @Test
    public void checkLikePaletteInvalidId() throws SQLException{
        assertThrows(Palette404Exception.class, () -> {paletteService.likePalette("", "123");});
    }

    @Test
    public void checkLikePaletteWrongId() throws SQLException{
        assertThrows(Palette404Exception.class, () -> {paletteService.likePalette("", AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS));});
    }

    @Test
    public void checkLikePaletteAlreadyLiked() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());
        assertEquals(1, PaletteTest.insertLikePreparedStatement(dataSource.getConnection(), user.getUserId(), palette.getPaletteId()).executeUpdate());

        assertThrows(PaletteLikeException.class, () -> {paletteService.likePalette(user.getUserId(), palette.getPaletteId());});
    }

    @Test
    public void checkUnlikePalette() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());
        assertEquals(1, PaletteTest.insertLikePreparedStatement(dataSource.getConnection(), user.getUserId(), palette.getPaletteId()).executeUpdate());

        paletteService.unlikePalette(user.getUserId(), palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM likes WHERE user_id = ? AND palette_id = ?");
        ps.setString(1, user.getUserId());
        ps.setString(2, palette.getPaletteId());
        ResultSet res = ps.executeQuery();

        assertFalse(res.next());
    }

    @Test
    public void checkUnlikePaletteInvalidId() throws SQLException{
        assertThrows(Palette404Exception.class, () -> {paletteService.unlikePalette("", "123");});
    }

    @Test
    public void checkUnlikePaletteWrongId() throws SQLException{
        assertThrows(Palette404Exception.class, () -> {paletteService.unlikePalette("", AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS));});
    }

    @Test
    public void checkUnlikePaletteNotLiked() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());
        
        assertThrows(PaletteLikeException.class, () -> {paletteService.unlikePalette(user.getUserId(), palette.getPaletteId());});
    }

    @Test
    public void checkDeletePalette() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        UserTest.loginUser(user, jwtUtil);

        paletteService.deletePalette(palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM palettes WHERE  palette_id = ?");
        ps.setString(1, palette.getPaletteId());
        ResultSet res = ps.executeQuery();

        assertFalse(res.next());
    }

    @Test
    public void checkDeletePaletteNotOwner() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(RestrictedAccessException.class, () -> {paletteService.deletePalette(palette.getPaletteId());});
    }

    @Test
    public void checkDeletePaletteAdmin() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        requester.setRole("admin");
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        paletteService.deletePalette(palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM palettes WHERE  palette_id = ?");
        ps.setString(1, palette.getPaletteId());
        ResultSet res = ps.executeQuery();

        assertFalse(res.next());
    }

}
