package net.palettehub.api.collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
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
import net.palettehub.api.user.User;

import static net.palettehub.api.user.UserRepositoryTest.getSampleUsers;
import static net.palettehub.api.user.UserRepositoryTest.insertUserPreparedStatement;

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
        assertEquals(insertUserPreparedStatement(dataSource.getConnection(), user).executeUpdate(), 1);

        Collection collection = getSampleColletions(1)[0];
        collection.setUserId(user.getUserId());
        String collection_id = collectionRepository.createCollection(collection);

        PreparedStatement ps = dataSource.getConnection().prepareStatement("SELECT * FROM collections WHERE collection_id = ?");
        ps.setString(1, collection_id);
        ResultSet res = ps.executeQuery();

        assertTrue(res.next());
        assertEquals(res.getString("user_id"), collection.getUserId());
        assertEquals(res.getString("name"), collection.getName());
    }

    @Test
    public void checkGetCollection(){

    }

}
