package net.palettehub.api.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.palettehub.api.MySQLContainerBaseTest;
import net.palettehub.api.palette.Palette;
import net.palettehub.api.palette.PaletteList;

import net.palettehub.api.AppTest;
import static net.palettehub.api.AppTest.deleteAllData;
import static net.palettehub.api.palette.PaletteRepositoryTest.getSamplePalettes;
import static net.palettehub.api.palette.PaletteRepositoryTest.insertPalettePreparedStatement;
import static net.palettehub.api.palette.PaletteRepositoryTest.insertLikePreparedStatement;

/**
 * Test class for testing all palette sprocs using a UserRepository.
 * <p>
 * DirtiesContext needed to fix 
 * <a href="https://stackoverflow.com/questions/59372048/testcontainers-hikari-and-failed-to-validate-connection-org-postgresql-jdbc-pgc?answertab=trending#tab-top">
 * testcontainers bug</a>.
 * 
 * @author Arthur Lewis
 * @see UserRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode=DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserRepositoryTest extends MySQLContainerBaseTest{
    
    @Autowired
    private UserRepository usersRepository;

    /**
     * Create a User object with dummy data for testing.
     * @return User object with dummy data.
     */
    public static User[] getSampleUsers(int len){
        User[] users = new User[len];
        for (int i = 0; i < users.length; i++){
            User user = new User();
            user.setUserId(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS));
            user.setGoogleId(AppTest.generateRandomString(24, AppTest.NUMBERS));
            user.setEmail(AppTest.generateRandomString(10, AppTest.LETTERS)+"@example.com");
            user.setName(AppTest.generateRandomString(8, AppTest.LETTERS) + " " + AppTest.generateRandomString(6, AppTest.LETTERS));
            user.setPictureUrl("https://www.example.com/"+AppTest.generateRandomString(20, AppTest.LETTERS));
            users[i] = user;
        }
        return users;
    }

    /**
     * Creates and returns a PrepraredStatement to insert a user into the users table.
     * @param conn SQL connection
     * @param user User object to insert
     * @return PreparedStatement to insert user ready to be executed.
     * @throws SQLException
     */
    public static PreparedStatement insertUserPreparedStatement(Connection conn, User user) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("INSERT INTO users (user_id, google_id, email, name, picture_url) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, user.getUserId());
        ps.setString(2, user.getGoogleId());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getName());
        ps.setString(5, user.getPictureUrl());
        return ps;
    }

    @After
    public void deleteRows() throws SQLException{
        deleteAllData(dataSource.getConnection());
    }

    @Test
    public void injectedComponentsAreNotNull() {
        assertNotEquals(dataSource, null);
        assertNotEquals(usersRepository, null);
    }

    @Test
    public void checkCreateUser() throws SQLException{
        User user = getSampleUsers(1)[0];
        String userId = usersRepository.createUser(user);
        user.setUserId(userId);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM users WHERE google_id = ?");
        ps.setString(1, user.getGoogleId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(res.getString("google_id"), user.getGoogleId());
        assertEquals(res.getString("email"), user.getEmail());
        assertEquals(res.getString("name"), user.getName());
        assertEquals(res.getString("picture_url"), user.getPictureUrl());
        assertEquals(res.getString("user_id"), user.getUserId());
    }

    @Test
    public void checkUserIdExists() throws SQLException{
        User user = getSampleUsers(1)[0];
        PreparedStatement ps = insertUserPreparedStatement(dataSource.getConnection(), user);

        assertEquals(ps.executeUpdate(), 1);
        assertNotEquals(usersRepository.getUserById(user.getUserId()), null);
    }

    @Test
    public void checkUserIdDoesNotExist() throws SQLException{
        User user = getSampleUsers(1)[0];
        PreparedStatement ps = insertUserPreparedStatement(dataSource.getConnection(), user);
        
        assertEquals(ps.executeUpdate(), 1);
        assertEquals(usersRepository.getUserById(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()})), null);
    }

    @Test
    public void checkUserGoogleIdExists() throws SQLException{
        User user = getSampleUsers(1)[0];
        PreparedStatement ps = insertUserPreparedStatement(dataSource.getConnection(), user);
        
        assertEquals(ps.executeUpdate(), 1);
        assertNotEquals(usersRepository.getUserByGoogleId(user.getGoogleId()), null);
    }

    @Test
    public void checkUserGoogleIdDoesNotExist() throws SQLException{
        User user = getSampleUsers(1)[0];
        PreparedStatement ps = insertUserPreparedStatement(dataSource.getConnection(), user);
        
        assertEquals(ps.executeUpdate(), 1);
        assertEquals(usersRepository.getUserById(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()})), null);
    }

    @Test
    public void checkUserLikedPalettes() throws SQLException{
        User user = getSampleUsers(1)[0];
        PreparedStatement ps = insertUserPreparedStatement(dataSource.getConnection(), user);
        assertEquals(ps.executeUpdate(), 1);

        Palette palette = getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        PreparedStatement ps2 = insertPalettePreparedStatement(dataSource.getConnection(), palette);
        assertEquals(ps2.executeUpdate(), 1);

        PreparedStatement ps3 = insertLikePreparedStatement(dataSource.getConnection(), user.getUserId(), palette.getPaletteId());
        assertEquals(ps3.executeUpdate(), 1);

        PaletteList paletteList = usersRepository.getLikedPalettes(user.getUserId(), user.getUserId(), 1);
        assertEquals(paletteList.getCount(), 1);
        Palette likedPalette = paletteList.getPalettes().get(0);
        assertEquals(likedPalette.getPaletteId(), palette.getPaletteId());
        assertEquals(likedPalette.getUserId(), palette.getUserId());
        assertEquals(likedPalette.getColor1(), palette.getColor1());
        assertEquals(likedPalette.getColor2(), palette.getColor2());
        assertEquals(likedPalette.getColor3(), palette.getColor3());
        assertEquals(likedPalette.getColor4(), palette.getColor4());
        assertEquals(likedPalette.getColor5(), palette.getColor5());
        // assertEquals(likedPalette.getLikes(), 1);
        // assertTrue(likedPalette.isLiked());
    }

}
