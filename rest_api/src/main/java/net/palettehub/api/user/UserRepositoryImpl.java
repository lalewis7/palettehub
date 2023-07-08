package net.palettehub.api.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import net.palettehub.api.palette.PaletteList;
import net.palettehub.api.palette.PaletteRepositoryImpl;

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

    @Override
    public PaletteList getLikedPalettes(String userId, int page){
        return PaletteRepositoryImpl.getPalettes(userId, page, "find_user_liked_palettes", em);
    }
    
}
