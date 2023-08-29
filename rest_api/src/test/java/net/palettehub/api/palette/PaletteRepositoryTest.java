package net.palettehub.api.palette;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.palettehub.api.MySQLContainerBaseTest;
import net.palettehub.api.user.User;
import net.palettehub.api.user.UserTest;
import net.palettehub.api.AppTest;
import static net.palettehub.api.AppTest.deleteAllData;

/**
 * Test class for testing all palette sprocs using a PaletteRepository.
 * <p>
 * DirtiesContext needed to fix 
 * <a href="https://stackoverflow.com/questions/59372048/testcontainers-hikari-and-failed-to-validate-connection-org-postgresql-jdbc-pgc?answertab=trending#tab-top">
 * testcontainers bug</a>.
 * 
 * @author Arthur Lewis
 * @see PaletteRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode=DirtiesContext.ClassMode.BEFORE_CLASS)
public class PaletteRepositoryTest extends MySQLContainerBaseTest {

    @Autowired
    private PaletteRepository paletteRepository;

    @After
    public void deleteRows() throws SQLException{
        deleteAllData(dataSource.getConnection());
    }

    @Test
    public void injectedComponentsAreNotNull() {
        assertNotEquals(dataSource, null);
        assertNotEquals(paletteRepository, null);
    }

    @Test
    public void checkNewPalettesOrder() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        PreparedStatement ps = UserTest.insertUserPreparedStatement(dataSource.getConnection(), user);
        assertEquals(ps.executeUpdate(), 1);

        Palette[] palettes = PaletteTest.getSamplePalettes(3);
        for (int i = 0; i < palettes.length; i++){
            palettes[i].setPosted(100000 - 1000*i);
            palettes[i].setUserId(user.getUserId());
            PreparedStatement ps2 = PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palettes[i]);
            assertEquals(ps2.executeUpdate(), 1);
        }

        PaletteList paletteList = paletteRepository.getNewPalettes("", 1);
        assertEquals(paletteList.getCount(), 3);
        List<Palette> testPalettes = paletteList.getPalettes();
        for (int i = 0; i < testPalettes.size(); i++){
            assertEquals(testPalettes.get(i).getPaletteId(), palettes[i].getPaletteId());
        }
    }

    @Test
    public void checkPopularPalettesOrder() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        for (int i = 0; i < users.length; i++){
            PreparedStatement ps = UserTest.insertUserPreparedStatement(dataSource.getConnection(), users[i]);
            assertEquals(ps.executeUpdate(), 1);
        }

        Palette[] palettes = PaletteTest.getSamplePalettes(3);
        for (int i = 0; i < palettes.length; i++){
            palettes[i].setPosted(100000 - 1000*i);
            palettes[i].setUserId(users[0].getUserId());
            PreparedStatement ps2 = PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palettes[i]);
            assertEquals(ps2.executeUpdate(), 1);
        }

        for (int p = 0; p < palettes.length; p++){
            for (int u = 0; u < users.length - (palettes.length - p - 1); u++){
                PreparedStatement ps = PaletteTest.insertLikePreparedStatement(dataSource.getConnection(), users[u].getUserId(), palettes[p].getPaletteId());
                assertEquals(ps.executeUpdate(), 1);
            }
        }

        PaletteList paletteList = paletteRepository.getPopularPalettes("", 1);
        assertEquals(paletteList.getCount(), 3);
        List<Palette> testPalettes = paletteList.getPalettes();
        for (int i = 0; i < testPalettes.size(); i++){
            assertEquals(testPalettes.get(i).getPaletteId(), palettes[palettes.length-i-1].getPaletteId());
        }
    }

    @Test
    public void checkPaletteIdExists() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate(), 1);
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        assertEquals(PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate(), 1);
        assertNotEquals(paletteRepository.getPaletteById("", palette.getPaletteId()), null);
    }

    @Test
    public void checkPaletteIdDoesNotExist() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate(), 1);
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        assertEquals(PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate(), 1);
        assertEquals(paletteRepository.getPaletteById("", AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS, new String[]{palette.getPaletteId()})), null);
    }

    @Test
    public void checkCreatePalette() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate(), 1);

        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        String palette_id = paletteRepository.createPalette(palette);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM palettes WHERE palette_id = ?");
        ps.setString(1, palette_id);
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
    public void checkDeletePalette() throws SQLException {
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        paletteRepository.deletePalette(palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM palettes WHERE palette_id = ?");
        ps.setString(1, palette.getPaletteId());
        ResultSet res = ps.executeQuery();
        assertEquals(false, res.next());
    }

    @Test
    public void checkLikePalette() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate(), 1);
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        assertEquals(PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate(), 1);
        paletteRepository.likePalette(user.getUserId(), palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM likes WHERE user_id = ? AND palette_id = ?");
        ps.setString(1, user.getUserId());
        ps.setString(2, palette.getPaletteId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
    }

    @Test
    public void checkUnlikePalette() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate(), 1);
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        assertEquals(PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate(), 1);
        assertEquals(PaletteTest.insertLikePreparedStatement(dataSource.getConnection(), user.getUserId(), palette.getPaletteId()).executeUpdate(), 1);
        paletteRepository.unlikePalette(user.getUserId(), palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM likes WHERE user_id = ? AND palette_id = ?");
        ps.setString(1, user.getUserId());
        ps.setString(2, palette.getPaletteId());
        ResultSet res = ps.executeQuery();

        assertFalse(res.next());
    }

}
