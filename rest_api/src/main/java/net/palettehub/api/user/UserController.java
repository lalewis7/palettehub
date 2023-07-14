package net.palettehub.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.palettehub.api.palette.PaletteList;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody GoogleAuth creds){
        return new ResponseEntity<String>(userService.authenticate(creds), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId){
        return new ResponseEntity<User>(userService.getUser(userId.equalsIgnoreCase("self") ? getUserId() : userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/likes")
    public ResponseEntity<PaletteList> getUserLikes(@PathVariable("userId") String userId, @RequestParam(name = "page", defaultValue = "1") String page){
        return new ResponseEntity<PaletteList>(userService.getLikedPalettes((userId.equalsIgnoreCase("self") ? getUserId() : userId), page), HttpStatus.OK);
    }

    private String getUserId(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
