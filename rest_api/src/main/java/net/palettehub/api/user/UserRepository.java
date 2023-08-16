package net.palettehub.api.user;

import org.springframework.stereotype.Repository;

import net.palettehub.api.collection.CollectionList;
import net.palettehub.api.palette.PaletteList;

/**
 * User repository interface.
 * 
 * @author Arthur Lewis
 */
@Repository
public interface UserRepository {
    
    // create user
    String createUser(User user);

    // get user by id
    User getUserById(String userId);

    // get user by google id
    User getUserByGoogleId(String googleId);

    // get palettes liked by user
    PaletteList getLikedPalettes(String userId, String requesterId, int page);

    // get palettes from user
    PaletteList getPalettes(String userId, String requesterId, int page);

    // edit user
    boolean editUser(String userId, User user);

    // get collections
    CollectionList getUserCollections(String userId, int page);

}
