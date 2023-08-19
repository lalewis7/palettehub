package net.palettehub.api.collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

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

import net.palettehub.api.AppTest;
import net.palettehub.api.MySQLContainerBaseTest;
import net.palettehub.api.palette.Palette;
import net.palettehub.api.palette.PaletteList;
import net.palettehub.api.user.User;

import static net.palettehub.api.user.UserRepositoryTest.getSampleUsers;
import static net.palettehub.api.user.UserRepositoryTest.insertUserPreparedStatement;
import static net.palettehub.api.palette.PaletteRepositoryTest.getSamplePalettes;
import static net.palettehub.api.palette.PaletteRepositoryTest.insertPalettePreparedStatement;

/**
 * Test class for testing all palette sprocs using a CollectionRepository.
 * <p>
 * DirtiesContext needed to fix 
 * <a href="https://stackoverflow.com/questions/59372048/testcontainers-hikari-and-failed-to-validate-connection-org-postgresql-jdbc-pgc?answertab=trending#tab-top">
 * testcontainers bug</a>.
 * 
 * @author Arthur Lewis
 * @see CollectionRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode=DirtiesContext.ClassMode.BEFORE_CLASS)
public class CollectionRepositoryTest extends MySQLContainerBaseTest{
    
    @Autowired
    private CollectionRepository collectionRepository;

    /**
     * Create a User object with dummy data for testing.
     * @return User object with dummy data.
     */
    public static Collection[] getSampleColletions(int len){
        Collection[] collections = new Collection[len];
        for (int i = 0; i < collections.length; i++){
            Collection collection = new Collection();
            collection.setCollectionId(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS));
            collection.setName(AppTest.generateRandomString(16, AppTest.LETTERS));
            collections[i] = collection;
        }
        return collections;
    }

    /**
     * Prepares a PreparedStatement object to insert a new collection.
     * @param conn SQL connection
     * @param collection collection data to be inserted
     * @return Insert PreparedStatement
     * @throws SQLException
     */
    public static PreparedStatement insertCollectionPreparedStatement(Connection conn, Collection collection) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("INSERT INTO collections " + 
            "(collection_id, user_id, name) VALUES (?, ?, ?)");
        ps.setString(1, collection.getCollectionId());
        ps.setString(2, collection.getUserId());
        ps.setString(3, collection.getName());
        return ps;
    }

    @After
    public void deleteRows() throws SQLException{
        AppTest.deleteAllData(dataSource.getConnection());
    }

    @Test
    public void injectedComponentsAreNotNull() {
        assertNotEquals(dataSource, null);
        assertNotEquals(collectionRepository, null);
    }

    @Test
    public void checkCreateCollection() throws SQLException{
        User user = getSampleUsers(1)[0];
        assertEquals(1, insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        String collection_id = collectionRepository.createCollection(collection);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, collection_id);
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(collection.getUserId(), res.getString("user_id"));
        assertEquals(collection.getName(), res.getString("name"));
    }

    @Test
    public void checkCollectionIdExists() throws SQLException{
        User user = getSampleUsers(1)[0];
        assertEquals(1, insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        assertEquals(1, insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        assertNotEquals(null, collectionRepository.getCollection(collection.getCollectionId()));
    }

    @Test
    public void checkCollectionIdDoesNotExists() throws SQLException{
        User user = getSampleUsers(1)[0];
        assertEquals(1, insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        assertEquals(1, insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        assertEquals(null, collectionRepository.getCollection(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS, new String[]{user.getUserId()})));
    }

    @Test
    public void checkEditCollection() throws SQLException {
        User user = getSampleUsers(1)[0];
        assertEquals(1, insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        assertEquals(1, insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        Collection changedCollection = getSampleColletions(1)[0];
        changedCollection.setCollectionId(collection.getCollectionId());
        changedCollection.setUserId(collection.getUserId());
        collectionRepository.editCollection(collection.getCollectionId(), changedCollection);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, changedCollection.getCollectionId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(changedCollection.getName(), res.getString("name"));
    }

    @Test
    public void checkDeleteCollection() throws SQLException {
        User user = getSampleUsers(1)[0];
        assertEquals(1, insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        assertEquals(1, insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        collectionRepository.deleteCollection(collection.getCollectionId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, collection.getCollectionId());
        ResultSet res = ps.executeQuery();
        assertEquals(false, res.next());
    }

    @Test
    public void checkAddPaletteToCollection() throws SQLException {
        User user = getSampleUsers(1)[0];
        assertEquals(1, insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        assertEquals(1, insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        Palette palette = getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        assertEquals(1, insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        collectionRepository.addToCollection(collection.getCollectionId(), palette.getPaletteId());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collection_palettes c JOIN palettes p ON c.palette_id = p.palette_id WHERE c.collection_id = ? AND c.palette_id = ?");
        ps.setString(1, collection.getCollectionId());
        ps.setString(2, palette.getPaletteId());
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(palette.getUserId(), res.getString("user_id"));
        assertEquals(palette.getColor1(), res.getString("color_1"));
        assertEquals(palette.getColor2(), res.getString("color_2"));
        assertEquals(palette.getColor3(), res.getString("color_3"));
        assertEquals(palette.getColor4(), res.getString("color_4"));
        assertEquals(palette.getColor5(), res.getString("color_5"));
    }

    @Test
    public void checkRemovePaletteFromCollection() throws SQLException {
        User user = getSampleUsers(1)[0];
        assertEquals(1, insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        assertEquals(1, insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        Palette palette = getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        assertEquals(1, insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("INSERT INTO collection_palettes (collection_id, palette_id) VALUES (?, ?)");
        ps.setString(1, collection.getCollectionId());
        ps.setString(2, palette.getPaletteId());
        assertEquals(1, ps.executeUpdate());

        PreparedStatement ps2 = dataSource.getConnection().prepareStatement("SELECT * FROM collection_palettes WHERE collection_id = ? AND palette_id = ?");
        ps2.setString(1, collection.getCollectionId());
        ps2.setString(2, palette.getPaletteId());
        ResultSet res = ps2.executeQuery();
        assertTrue(res.next());

        collectionRepository.removeFromCollection(collection.getCollectionId(), palette.getPaletteId());

        PreparedStatement ps3 = dataSource.getConnection().prepareStatement("SELECT * FROM collection_palettes WHERE collection_id = ? AND palette_id = ?");
        ps3.setString(1, collection.getCollectionId());
        ps3.setString(2, palette.getPaletteId());
        ResultSet res2 = ps2.executeQuery();
        assertFalse(res2.next());
    }

    @Test
    public void checkCollectionPalettes() throws SQLException {
        User user = getSampleUsers(1)[0];
        assertEquals(1, insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate());

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        assertEquals(1, insertCollectionPreparedStatement(dataSource.getConnection(), collection).executeUpdate());

        Palette palette = getSamplePalettes(1)[0];
        palette.setUserId(user.getUserId());
        assertEquals(1, insertPalettePreparedStatement(dataSource.getConnection(), palette).executeUpdate());

        PreparedStatement ps = dataSource.getConnection().prepareStatement("INSERT INTO collection_palettes (collection_id, palette_id) VALUES (?, ?)");
        ps.setString(1, collection.getCollectionId());
        ps.setString(2, palette.getPaletteId());
        assertEquals(1, ps.executeUpdate());

        PreparedStatement ps2 = dataSource.getConnection().prepareStatement("SELECT * FROM collection_palettes WHERE collection_id = ? AND palette_id = ?");
        ps2.setString(1, collection.getCollectionId());
        ps2.setString(2, palette.getPaletteId());
        ResultSet res = ps2.executeQuery();
        assertTrue(res.next());

        PaletteList paletteList = collectionRepository.getCollectionPalettes(collection.getCollectionId(), user.getUserId(), 1);

        assertEquals(1, paletteList.getCount());
        Palette paletteRes = paletteList.getPalettes().get(0);
        assertEquals(palette.getUserId(), paletteRes.getUserId());
        assertEquals(palette.getColor1(), paletteRes.getColor1());
        assertEquals(palette.getColor2(), paletteRes.getColor2());
        assertEquals(palette.getColor3(), paletteRes.getColor3());
        assertEquals(palette.getColor4(), paletteRes.getColor4());
        assertEquals(palette.getColor5(), paletteRes.getColor5());
    }

}
