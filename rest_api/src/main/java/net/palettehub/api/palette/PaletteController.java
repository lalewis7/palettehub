package net.palettehub.api.palette;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaletteController {
    
    @GetMapping("/palettes")
    public String getPalettes(@RequestParam("sort") String sort, @RequestParam("page") String page){
        return "Not implemented yet.";
    }

    

}
