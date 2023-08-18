package net.palettehub.api.user;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import net.palettehub.api.collection.CollectionList;
import net.palettehub.api.jwt.JwtUtil;
import net.palettehub.api.palette.PaletteList;
import net.palettehub.api.palette.exception.PageValueInvalidException;
import net.palettehub.api.user.exception.GoogleAuthException;
import net.palettehub.api.user.exception.User404Exception;
import net.palettehub.exception.RestrictedAccessException;

/**
 * User service code with the business logic of the user endpoints.
 * 
 * @author Arthur Lewis
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
	private JwtUtil jwtUtil;

    @Value("${google.clientid}")
	private String clientId;

    public String authenticate(GoogleAuth creds){
        User googleUser;
        String errMsg = "Google authentication failed.";
        try {
            googleUser = getGoogleProfile(creds);
        } catch (GeneralSecurityException e) {
            throw new GoogleAuthException(errMsg);
        } catch (IOException e) {
            throw new GoogleAuthException(errMsg);
        }
        if (googleUser == null) throw new GoogleAuthException(errMsg);
        User user = createUser(googleUser);
        return jwtUtil.generateToken(user.getUserId(), jwtUtil.getClaims(user.getRole()));
    }

    private User createUser(User user){
        User userLookup = userRepository.getUserByGoogleId(user.getGoogleId());
        // user doesn't exist (sign up)
        if (userLookup == null) {
            String id = userRepository.createUser(user);
            User newUser = new User();
            newUser.setUserId(id);
            newUser.setRole("user");
            return newUser;
        }
        // user already exists (logging in)
        else {
            // update image
            if (!user.getPictureUrl().equals(userLookup.getPictureUrl())){
                userLookup.setPictureUrl(user.getPictureUrl());
                userRepository.editUser(userLookup.getUserId(), userLookup);
            }
            return userLookup;
        }
    }

    private User getGoogleProfile(GoogleAuth creds) throws GeneralSecurityException, IOException {
        // https://developers.google.com/identity/gsi/web/guides/verify-google-id-token
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            // Specify the CLIENT_ID of the app that accesses the backend:
            .setAudience(Collections.singletonList(clientId))
            // Or, if multiple clients access the backend:
            //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
            .build();
        System.out.println(creds);
        GoogleIdToken idToken = verifier.verify(creds.getCredential());
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Get profile information from payload
            User user = new User();
            user.setEmail(payload.getEmail());
            user.setName((String) payload.get("name"));
            user.setPictureUrl((String) payload.get("picture"));
            user.setGoogleId((String) payload.getSubject());
            System.out.println(user);
            return user;
        }
        System.out.println("Invalid Google ID token.");
        return null;
    }

    public User getUser(String userId){
        // not uuid
        if (userId.length() != 32)
            throw new User404Exception("User not found.");
        User user = userRepository.getUserById(userId);
        // 404 error
        if (user == null)
            throw new User404Exception("User not found.");
        else {
            // remove user image if censored
            // if picture not visible and someone else or not admin
            if (!(user.getShowPicture() && !getUserId().equals(userId)) || !hasAuthority("ROLE_ADMIN")){
                user.setPictureUrl("");
            }
            return user;
        }
    }

    public PaletteList getLikedPalettes(String userId, String page){
        // check page value
        int pageValue;
        if (page.equals("")){ // no page given set to 1
            pageValue = 1;
        }
        else { // check passed page value
            try {pageValue = Integer.parseInt(page);}
            catch (NumberFormatException e) {throw new PageValueInvalidException("Page value invalid.");}
        }
        // not uuid
        if (userId.length() != 32)
            throw new User404Exception("User not found.");
        // return list
        return userRepository.getLikedPalettes(userId, getUserId(), pageValue);
    }

    // edit user
    public void editUser(String userId, User user){
        // not uuid
        if (userId.length() != 32)
            throw new User404Exception("User not found.");
        // check if user exists
        User userLookup = userRepository.getUserById(userId);
        if (userLookup == null)
            throw new User404Exception("User not found.");
        // not allowed to manually change picture url
        user.setPictureUrl(userLookup.getPictureUrl());
        // owner or admin
        if (userId.equals(getUserId()) || hasAuthority("ROLE_ADMIN"))
            userRepository.editUser(userId, user);
        else 
            throw new RestrictedAccessException("You do not have access to edit this user.");
    }

    // user palettes
    public PaletteList getUserPalettes(String userId, String page){
        // check page value
        int pageValue;
        if (page.equals("")){ // no page given set to 1
            pageValue = 1;
        }
        else { // check passed page value
            try {pageValue = Integer.parseInt(page);}
            catch (NumberFormatException e) {throw new PageValueInvalidException("Page value invalid.");}
        }
        // not uuid
        if (userId.length() != 32)
            throw new User404Exception("User not found.");
        // return list
        return userRepository.getPalettes(userId, getUserId(), pageValue);
    }

    // user collections
    public CollectionList getCollections(String userId, String page){
        // check page value
        int pageValue;
        if (page.equals("")){ // no page given set to 1
            pageValue = 1;
        }
        else { // check passed page value
            try {pageValue = Integer.parseInt(page);}
            catch (NumberFormatException e) {throw new PageValueInvalidException("Page value invalid.");}
        }
        // not uuid
        if (userId.length() != 32)
            throw new User404Exception("User not found.");
        // return list
        return userRepository.getUserCollections(userId, pageValue);
    }

    private boolean hasAuthority(Object authority){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(authority);
    }

    private String getUserId(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
