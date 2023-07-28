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

import net.palettehub.api.jwt.JwtUtil;
import net.palettehub.api.palette.PaletteList;
import net.palettehub.api.palette.exception.PageValueInvalidException;
import net.palettehub.api.user.exception.GoogleAuthException;
import net.palettehub.api.user.exception.RestrictedAccessException;
import net.palettehub.api.user.exception.User404Exception;

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
        User user;
        String errMsg = "Google authentication failed.";
        try {
            user = getGoogleProfile(creds);
        } catch (GeneralSecurityException e) {
            throw new GoogleAuthException(errMsg);
        } catch (IOException e) {
            throw new GoogleAuthException(errMsg);
        }
        if (user == null) throw new GoogleAuthException(errMsg);
        String userId = createUser(user);
        return jwtUtil.generateToken(userId);
    }

    private String createUser(User user){
        User userLookup = userRepository.getUserByGoogleId(user.getGoogleId());
        // user doesn't exist (sign up)
        if (userLookup == null) 
            return userRepository.createUser(user);
        // user already exists (logging in)
        else 
            return userLookup.getUserId();
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
        // does not have access
        if (!userId.equals(getUserId())) 
            throw new RestrictedAccessException("You do not have access to this users profile.");
        // not uuid
        if (userId.length() != 32)
            throw new User404Exception("User not found.");
        User user = userRepository.getUserById(userId);
        // 404 error
        if (user == null)
            throw new User404Exception("User not found.");
        else
            return user;
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
        // does not have access
        if (!userId.equals(getUserId()))
            throw new RestrictedAccessException("You do not have access to this users profile.");
        // not uuid
        if (userId.length() != 32)
            throw new User404Exception("User not found.");
        // return list
        return userRepository.getLikedPalettes(userId, pageValue);
    }

    private String getUserId(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
