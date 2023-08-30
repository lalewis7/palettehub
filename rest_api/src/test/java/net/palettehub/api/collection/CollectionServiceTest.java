package net.palettehub.api.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.palettehub.api.AppTest;
import net.palettehub.api.MySQLContainerBaseTest;
import net.palettehub.api.collection.exception.Collection404Exception;
import net.palettehub.api.jwt.JwtUtil;
import net.palettehub.api.palette.Palette;
import net.palettehub.api.palette.PaletteTest;
import net.palettehub.api.user.User;
import net.palettehub.api.user.UserTest;
import net.palettehub.exception.RestrictedAccessException;

@RunWith(SpringJUnit4ClassRunner.class)
public class CollectionServiceTest extends MySQLContainerBaseTest{
    
    @Autowired
    private CollectionService collectionService;

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
        assertNotEquals(null, collectionService);
    }

    @Test
    public void checkCreateCollection() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        String collectionId = collectionService.createCollection(user.getUserId(), collection);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, collectionId);
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(collection.getUserId(), res.getString("user_id"));
        assertEquals(collection.getName(), res.getString("name"));
    }

    @Test
    public void checkGetCollection() throws SQLException {
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());
        PreparedStatement ps = dataSource.getConnection().prepareStatement("INSERT INTO collection_palettes (collection_id, palette_id) VALUES (?, ?)");
        ps.setString(1, collection.getCollectionId());
        ps.setString(2, palette.getPaletteId());
        assertEquals(1, ps.executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        Collection col = collectionService.getCollection(collection.getCollectionId(), "1");

        assertEquals(collection.getUserId(), col.getUserId());
        assertEquals(collection.getName(), col.getName());
        assertEquals(1, col.getCount());

        Palette pal = col.getPalettes().get(0);
        assertEquals(palette.getColor1(), pal.getColor1());
        assertEquals(palette.getColor2(), pal.getColor2());
        assertEquals(palette.getColor3(), pal.getColor3());
        assertEquals(palette.getColor4(), pal.getColor4());
        assertEquals(palette.getColor5(), pal.getColor5());
    }

    @Test
    public void checkGetCollectionInvalidId() throws SQLException{
        assertThrows(Collection404Exception.class, () -> {collectionService.getCollection("123", "1");});
    }

    @Test
    public void checkGetCollectionWrongId() throws SQLException{
        assertThrows(Collection404Exception.class, () -> {collectionService.getCollection(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS), "1");});
    }

    @Test
    public void checkEditCollection() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Collection[] collections = CollectionTest.getSampleColletions(2);
        Collection collection = collections[0];
        Collection editedCollection = collections[1];
        collection.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        UserTest.loginUser(user, jwtUtil);

        collectionService.editCollection(collection.getCollectionId(), editedCollection);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, collection.getCollectionId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(editedCollection.getName(), res.getString("name"));
    }

    @Test
    public void checkEditCollectionNotOwner() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(RestrictedAccessException.class, () -> {collectionService.editCollection(collection.getCollectionId(), collection);});
    }

    @Test
    public void checkEditCollectionAdmin() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        requester.setRole("admin");
        Collection[] collections = CollectionTest.getSampleColletions(2);
        Collection collection = collections[0];
        Collection editedCollection = collections[1];
        collection.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        collectionService.editCollection(collection.getCollectionId(), editedCollection);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, collection.getCollectionId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(editedCollection.getName(), res.getString("name"));
    }

    @Test
    public void checkDeleteCollection() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        UserTest.loginUser(user, jwtUtil);

        collectionService.deleteCollection(collection.getCollectionId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, collection.getCollectionId());
        ResultSet res = ps.executeQuery();

        assertFalse(res.next());
    }

    @Test
    public void checkDeleteCollectionNotOwner() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(RestrictedAccessException.class, () -> {collectionService.deleteCollection(collection.getCollectionId());});
    }

    @Test
    public void checkDeleteCollectionAdmin() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        requester.setRole("admin");
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        collectionService.deleteCollection(collection.getCollectionId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, collection.getCollectionId());
        ResultSet res = ps.executeQuery();

        assertFalse(res.next());
    }

    @Test
    public void checkAddToCollection() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        UserTest.loginUser(user, jwtUtil);

        collectionService.addToCollection(collection.getCollectionId(), palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collection_palettes WHERE collection_id = ? AND palette_id = ?");
        ps.setString(1, collection.getCollectionId());
        ps.setString(2, palette.getPaletteId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
    }

    @Test
    public void checkAddToCollectionNotOwner() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(RestrictedAccessException.class, () -> {collectionService.addToCollection(collection.getCollectionId(), palette.getPaletteId());});
    }

    @Test
    public void checkRemoveFromCollection() throws SQLException{
        User user = UserTest.getSampleUsers(1)[0];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());
        PreparedStatement ps = dataSource.getConnection().prepareStatement("INSERT INTO collection_palettes (collection_id, palette_id) VALUES (?, ?)");
        ps.setString(1, collection.getCollectionId());
        ps.setString(2, palette.getPaletteId());
        assertEquals(1, ps.executeUpdate());

        UserTest.loginUser(user, jwtUtil);

        collectionService.removeFromCollection(collection.getCollectionId(), palette.getPaletteId());

        PreparedStatement ps2 = dataSource.getConnection().prepareStatement("SELECT * FROM collection_palettes WHERE collection_id = ? AND palette_id = ?");
        ps2.setString(1, collection.getCollectionId());
        ps2.setString(2, palette.getPaletteId());
        ResultSet res = ps2.executeQuery();

        assertFalse(res.next());
    }

    @Test
    public void checkRemoveFromCollectionNotOwner() throws SQLException{
        User[] users = UserTest.getSampleUsers(2);
        User user = users[0];
        User requester = users[1];
        Collection collection = CollectionTest.getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        Palette palette = PaletteTest.getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());

        assertEquals(1, UserTest.insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());
        assertEquals(1, CollectionTest.insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());
        assertEquals(1, PaletteTest.insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());
        PreparedStatement ps = dataSource.getConnection().prepareStatement("INSERT INTO collection_palettes (collection_id, palette_id) VALUES (?, ?)");
        ps.setString(1, collection.getCollectionId());
        ps.setString(2, palette.getPaletteId());
        assertEquals(1, ps.executeUpdate());

        UserTest.loginUser(requester, jwtUtil);

        assertThrows(RestrictedAccessException.class, () -> {collectionService.removeFromCollection(collection.getCollectionId(), palette.getPaletteId());});
    }

}
