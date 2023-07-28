package net.palettehub.api.user;

import org.springframework.stereotype.Repository;

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
    PaletteList getLikedPalettes(String userId, int page);

}
