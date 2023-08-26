package net.palettehub.api.collection;

import org.springframework.stereotype.Repository;

import net.palettehub.api.palette.PaletteList;

/**
 * Collection repository interface.
 * 
 * @author Arthur Lewis
 */
@Repository
public interface CollectionRepository {
    
    String createCollection(Collection collection);

    boolean deleteCollection(String id);

    Collection getCollection(String id);

    boolean editCollection(String id, Collection collection);

    boolean addToCollection(String collectionId, String paletteId);

    boolean removeFromCollection(String collectionId, String paletteId);

    PaletteList getCollectionPalettes(String id, String userId, int page);

}
