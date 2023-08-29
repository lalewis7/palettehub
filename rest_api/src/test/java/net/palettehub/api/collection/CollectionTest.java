package net.palettehub.api.collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.palettehub.api.AppTest;

public class CollectionTest {
    
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

}
