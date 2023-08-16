package net.palettehub.api.collection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

/**
 * Implementation of collection repository instance. Calls mysql sprocs.
 * 
 * @author Arthur Lewis
 * @see CollectionRepository
 */
@Repository
public class CollectionRepositoryImpl implements CollectionRepository {

    public static final int PAGE_SIZE = 50;

    @Autowired
    private EntityManager em;

    @Override
    public String createCollection(Collection collection) {
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery("create_collection", Collection.class);

        // register params
        query.registerStoredProcedureParameter("userId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("collectionName", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("collectionId", String.class, ParameterMode.OUT);

        // set params
        query.setParameter("userId", collection.getUserId());
        query.setParameter("collectionName", collection.getName());

        return (String) query.getOutputParameterValue("collectionId");
    }

    @Override
    public boolean deleteCollection(String id) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("delete_collection", Collection.class);

        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);

        query.setParameter("id", id);

        return query.executeUpdate() > 0;
    }

    public static Collection getCollection(String id, EntityManager em){
        StoredProcedureQuery query = em.createStoredProcedureQuery("get_collection_by_id", Collection.class);

        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);

        query.setParameter("id", id);

        return (Collection) query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public Collection getCollection(String id) {
        return getCollection(id, em);
    }

    @Override
    public boolean editCollection(String id, Collection collection) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("edit_collection", Collection.class);

        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("collectionName", String.class, ParameterMode.IN);

        query.setParameter("id", id);
        query.setParameter("collectionName", collection.getName());

        return query.executeUpdate() > 0;
    }

    private boolean toCollection(String collectionId, String paletteId, String sproc){
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery(sproc, Collection.class);

        // register params
        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("paletteId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", String.class, ParameterMode.OUT);

        // set params
        query.setParameter("id", collectionId);
        query.setParameter("paletteId", paletteId);

        return ((Number) query.getOutputParameterValue("result")).intValue() == 1;
    }

    @Override
    public boolean addToCollection(String collectionId, String paletteId) {
        return toCollection(collectionId, paletteId, "add_to_collection");
    }

    @Override
    public boolean removeFromCollection(String collectionId, String paletteId) {
        return toCollection(collectionId, paletteId, "remove_from_collection");
    }
    
}
