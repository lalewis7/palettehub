package net.palettehub.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @GetMapping("/users/{userId}/likes")
    public String getUserLikes(@PathVariable("userId") String userId){
        return "Not implemented yet.";
    }

}
