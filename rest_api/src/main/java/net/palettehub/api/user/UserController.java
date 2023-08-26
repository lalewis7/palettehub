package net.palettehub.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.palettehub.api.collection.CollectionList;
import net.palettehub.api.palette.PaletteList;

/**
 * User rest controller endpoints.
 * 
 * @author Arthur Lewis
 */
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

    @PutMapping("/users/{userId}")
    public ResponseEntity<String> putUser(@PathVariable("userId") String userId, @Valid @RequestBody User user){
        userService.editUser(userId, user);
        return new ResponseEntity<String>("Changes saved.", HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/palettes")
    public ResponseEntity<PaletteList> getUserPalettes(@PathVariable("userId") String userId, @RequestParam(name = "page", defaultValue = "1") String page){
        return new ResponseEntity<PaletteList>(userService.getUserPalettes((userId.equalsIgnoreCase("self") ? getUserId() : userId), page), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/collections")
    public ResponseEntity<CollectionList> getUserCollections(@PathVariable("userId") String userId, @RequestParam(name = "page", defaultValue = "1") String page){
        return new ResponseEntity<CollectionList>(userService.getCollections((userId.equalsIgnoreCase("self") ? getUserId() : userId), page), HttpStatus.OK);
    }

    private String getUserId(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
