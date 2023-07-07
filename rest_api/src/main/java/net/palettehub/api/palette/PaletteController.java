package net.palettehub.api.palette;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaletteController {

    @Autowired
    private PaletteService paletteService;
    
    @GetMapping("/palettes")
    public String getPalettes(@RequestParam("sort") String sort, @RequestParam("page") String page){
        return "Not implemented yet.";
    }

    @PostMapping("/palettes")
    public String postPalette(){
        return "Not implemented yet.";
    }

    @GetMapping("/palettes/{paletteId}")
    public String getPalette(@PathVariable("paletteId") String paletteId){
        return "Not implemented yet.";
    }

    @PostMapping("/palettes/{palettedId}/like")
    public String likePalette(@PathVariable("paletteId") String paletteId){
        return "Not implemented yet.";
    }

    @DeleteMapping("/palettes/{palettedId}/like")
    public String unlikePalette(@PathVariable("paletteId") String paletteId){
        return "Not implemented yet.";
    }

}
