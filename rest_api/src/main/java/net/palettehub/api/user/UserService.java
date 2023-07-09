package net.palettehub.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.palettehub.api.jwt.JwtUtil;
import net.palettehub.api.palette.PageValueInvalidException;
import net.palettehub.api.palette.PaletteList;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
	private JwtUtil jwtUtil;

    public String authenticate(User user){
        String userId = createUser(user);
        return jwtUtil.generateToken(userId);
    }

    public String createUser(User user){
        User userLookup = userRepository.getUserByGoogleId(user.getGoogleId());
        // user doesn't exist (sign up)
        if (userLookup == null) 
            return userRepository.createUser(user);
        // user already exists (logging in)
        else 
            return userLookup.getUserId();
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
        // return list
        return userRepository.getLikedPalettes(userId, pageValue);
    }

}
