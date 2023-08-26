package net.palettehub.api.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import net.palettehub.api.collection.Collection;
import net.palettehub.api.collection.CollectionList;
import net.palettehub.api.collection.CollectionRepositoryImpl;
import net.palettehub.api.palette.Palette;
import net.palettehub.api.palette.PaletteList;
import net.palettehub.api.palette.PaletteRepositoryImpl;

/**
 * Implementation of User repository interface.
 * 
 * @author Arthur Lewis
 * @see UserRepository
 */
@Repository
public class UserRepositoryImpl implements UserRepository{
    
    @Autowired
    private EntityManager em;

    @Override
    public String createUser(User user) {
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery("create_user", User.class);

        // register params
        query.registerStoredProcedureParameter("googleId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newName", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pictureUrl", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newEmail", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("userId", String.class, ParameterMode.OUT);

        // set params
        query.setParameter("googleId", user.getGoogleId());
        query.setParameter("newName", user.getName());
        query.setParameter("pictureUrl", user.getPictureUrl());
        query.setParameter("newEmail", user.getEmail());

        // return new id
        return (String) query.getOutputParameterValue("userId");
    }

    @Override
    public User getUserById(String userId) {
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery("get_user_by_id", User.class);

        // register params
        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);

        // set params
        query.setParameter("id", userId);

        // get result
        return (User) query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public User getUserByGoogleId(String googleId) {
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery("get_user_by_gid", User.class);

        // register params
        query.registerStoredProcedureParameter("gid", String.class, ParameterMode.IN);

        // set params
        query.setParameter("gid", googleId);

        // get result
        return (User) query.getResultStream().findFirst().orElse(null);
    }

    private PaletteList userPaletteList(String sproc, String userId, String requesterId, int page){
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery(sproc, Palette.class);
        
        // register params
        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("userId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pageSize", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pageNum", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("count", Integer.class, ParameterMode.OUT);

        // set params
        query.setParameter("id", userId);
        query.setParameter("userId", requesterId);
        query.setParameter("pageSize", PaletteRepositoryImpl.PAGE_SIZE);
        query.setParameter("pageNum", page);

        // get output param count
        int count = ((Number) query.getOutputParameterValue("count")).intValue();

        // get result list
        @SuppressWarnings("unchecked")
        List<Palette> palettes = query.getResultList();

        // return palette list
        return new PaletteList(palettes, count);
    }

    @Override
    public PaletteList getLikedPalettes(String userId, String requesterId, int page){
        return userPaletteList("find_user_liked_palettes", userId, requesterId, page);
    }

    @Override
    public PaletteList getPalettes(String userId, String requesterId, int page){
        return userPaletteList("find_user_palettes", userId, requesterId, page);
    }

    @Override
    public boolean editUser(String userId, User user){
        StoredProcedureQuery query = em.createStoredProcedureQuery("edit_user", User.class);

        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newName", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newPicture", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("showPicture", Boolean.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newBannerColor1", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newBannerColor2", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", Integer.class, ParameterMode.OUT);

        query.setParameter("id", userId);
        query.setParameter("newName", user.getName());
        query.setParameter("newPicture", user.getPictureUrl());
        query.setParameter("showPicture", user.getShowPicture());
        query.setParameter("newBannerColor1", user.getBannerColorLeft());
        query.setParameter("newBannerColor2", user.getBannerColorRight());

        return ((Number) query.getOutputParameterValue("result")).intValue() > 0;
    }

    @Override
    public CollectionList getUserCollections(String userId, String requesterId, int page){
        StoredProcedureQuery query = em.createStoredProcedureQuery("find_user_collections", Collection.class);

        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pageSize", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pageNum", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("count", Integer.class, ParameterMode.OUT);

        query.setParameter("id", userId);
        query.setParameter("pageSize", CollectionRepositoryImpl.PAGE_SIZE);
        query.setParameter("pageNum", page);

        int count = ((Number) query.getOutputParameterValue("count")).intValue();

        @SuppressWarnings("unchecked")
        List<Collection> collections = query.getResultList();

        for (int i = 0; i < collections.size(); i++){
            PaletteList paletteList = CollectionRepositoryImpl.getCollectionPalettes(collections.get(i).getCollectionId(), requesterId, 1, em);
            collections.get(i).setCount(paletteList.getCount());
            collections.get(i).setPalettes(paletteList.getPalettes());
        }

        return new CollectionList(collections, count);

    }
    
}
