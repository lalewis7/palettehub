package net.palettehub.api.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.palettehub.api.AppTest;
import net.palettehub.api.MySQLContainerBaseTest;
import net.palettehub.api.collection.Collection;
import net.palettehub.api.collection.CollectionList;
import net.palettehub.api.collection.CollectionTest;
import net.palettehub.api.jwt.JwtUtil;
import net.palettehub.api.palette.Palette;
import net.palettehub.api.palette.PaletteList;
import net.palettehub.api.palette.PaletteTest;
import net.palettehub.api.palette.exception.PageValueInvalidException;
import net.palettehub.api.user.exception.User404Exception;
import net.palettehub.exception.RestrictedAccessException;

import static net.palettehub.api.AppTest.deleteAllData;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest extends MySQLContainerBaseTest{
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @After
    public void deleteRows() throws SQLException{
        deleteAllData(dataSource.getConnection());
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void injectedComponentsAreNotNull() {
        assertNotEquals(dataSource, null);
        assertNotEquals(userService, null);
    }

    @Test
    public void checkGetUser() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        UserTest.loginUser(user, jwtUtil);

        assertEquals(userService.getUser(user.getUserId()).getName(), user.getName());
    }

    @Test
    public void checkGetUserPictureRemoved() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        user.setShowPicture(false);
        User requester = users[1];
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), requester).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertEquals("", userService.getUser(user.getUserId()).getPictureUrl());
    }

    @Test
    public void checkGetUserPictureRemovedAdmin() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        user.setShowPicture(false);
        User requester = users[1];
        requester.setRole("admin");

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), requester).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertEquals(user.getPictureUrl(), userService.getUser(user.getUserId()).getPictureUrl());
    }

    @Test
    public void checkGetUserInvalidId() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        UserTest.loginUser(user, jwtUtil);
        // 31 char len id
        assertThrows(User404Exception.class, () -> {userService.getUser(AppTest.generateRandomString(31, AppTest.HEX, new String[]{user.getUserId()}));});
    }

    @Test
    public void checkGetUserWrongId() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        UserTest.loginUser(user, jwtUtil);
        assertThrows(User404Exception.class, () -> {userService.getUser(AppTest.generateRandomString(32, AppTest.HEX, new String[]{user.getUserId()}));});
    }

    @Test
    public void checkEditUser() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User editedUser = users[1];
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        UserTest.loginUser(user, jwtUtil);

        userService.editUser(user.getUserId(), editedUser);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM users WHERE user_id = ?");
        ps.setString(1, user.getUserId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(editedUser.getName(), res.getString("name"));
        assertEquals(editedUser.getBannerColorLeft(), res.getString("banner_color_1"));
        assertEquals(editedUser.getBannerColorRight(), res.getString("banner_color_2"));
        assertEquals(user.getUserId(), res.getString("user_id"));
    }

    @Test
    public void checkEditUserNotOwner() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), requester).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(RestrictedAccessException.class, () -> {userService.editUser(user.getUserId(), user);});
    }

    @Test
    public void checkEditUserAdmin() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        requester.setRole("admin");

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), requester).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        userService.editUser(user.getUserId(), user);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM users WHERE user_id = ?");
        ps.setString(1, user.getUserId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(res.getString("google_id"), user.getGoogleId());
        assertEquals(res.getString("name"), user.getName());
        assertEquals(res.getString("picture_url"), user.getPictureUrl());
        assertEquals(res.getString("user_id"), user.getUserId());
    }

    @Test
    public void checkGetUserPalettes() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        PaletteList pl = userService.getUserPalettes(user.getUserId(), "1");

        assertEquals(1, pl.getCount());

        Palette pal = pl.getPalettes().get(0);
        assertEquals(palette.getColor1(), pal.getColor1());
        assertEquals(palette.getColor2(), pal.getColor2());
        assertEquals(palette.getColor3(), pal.getColor3());
        assertEquals(palette.getColor4(), pal.getColor4());
        assertEquals(palette.getColor5(), pal.getColor5());
    }

    @Test
    public void checkGetUserPalettesWrongId() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(User404Exception.class, () -> {userService.getUserPalettes(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()}), "1");});
    }

    @Test
    public void checkGetUserPalettesInvalidId() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        // 31 char len id
        assertThrows(User404Exception.class, () -> {userService.getUserPalettes(AppTest.generateRandomString(31, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()}), "1");});
    }

    @Test
    public void checkGetUserPalettesInvalidPage() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(PageValueInvalidException.class, () -> {userService.getUserPalettes(user.getUserId(), "a");});
    }

    @Test
    public void checkGetUserLikedPalettes() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());
        assertEquals(1, PaletteTest.insertLikePreparedStatement(dataSource.getConnection(), user.getUserId(), palette.getPaletteId()).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        PaletteList pl = userService.getUserPalettes(user.getUserId(), "1");

        assertEquals(1, pl.getCount());

        Palette pal = pl.getPalettes().get(0);
        assertEquals(palette.getColor1(), pal.getColor1());
        assertEquals(palette.getColor2(), pal.getColor2());
        assertEquals(palette.getColor3(), pal.getColor3());
        assertEquals(palette.getColor4(), pal.getColor4());
        assertEquals(palette.getColor5(), pal.getColor5());
    }

    @Test
    public void checkGetUserLikedPalettesWrongId() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(User404Exception.class, () -> {userService.getLikedPalettes(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()}), "1");});
    }

    @Test
    public void checkGetUserLikedPalettesInvalidId() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        // 31 char len id
        assertThrows(User404Exception.class, () -> {userService.getLikedPalettes(AppTest.generateRandomString(31, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()}), "1");});
    }

    @Test
    public void checkGetUserLikedPalettesInvalidPage() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(PageValueInvalidException.class, () -> {userService.getLikedPalettes(user.getUserId(), "a");});
    }

    @Test
    public void checkGetUserCollections() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        CollectionList cl = userService.getCollections(user.getUserId(), "1");

        assertEquals(1, cl.getCount());

        Collection col = cl.getCollections().get(0);
        assertEquals(collection.getName(), col.getName());
    }

    @Test
    public void checkGetUserCollectionsWrongId() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(User404Exception.class, () -> {userService.getCollections(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()}), "1");});
    }

    @Test
    public void checkGetUserCollectionsInvalidId() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        // 31 char len id
        assertThrows(User404Exception.class, () -> {userService.getCollections(AppTest.generateRandomString(31, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()}), "1");});
    }

    @Test
    public void checkGetUserCollectionsInvalidPage() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(PageValueInvalidException.class, () -> {userService.getCollections(user.getUserId(), "a");});
    }

}
