package net.palettehub.api.palette;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class PaletteController {

    @Autowired
    private PaletteService paletteService;
    
    @GetMapping("/palettes")
    public ResponseEntity<PaletteList> getPalettes(@RequestParam("sort") String sort, @RequestParam(name = "page", defaultValue = "1") String page) {
        return new ResponseEntity<PaletteList>(paletteService.getPalettes(getUserId(), sort, page), HttpStatus.OK);
    }

    @PostMapping("/palettes")
    public ResponseEntity<String> postPalette(@Valid @RequestBody Palette palette){
        return new ResponseEntity<String>(paletteService.createPalette(getUserId(), palette), HttpStatus.CREATED);
    }

    @GetMapping("/palettes/{paletteId}")
    public ResponseEntity<Palette> getPalette(@PathVariable("paletteId") String paletteId){
        return new ResponseEntity<Palette>(paletteService.getPalette(getUserId(), paletteId), HttpStatus.OK);
    }

    @PostMapping("/palettes/{paletteId}/like")
    public ResponseEntity<String> likePalette(@PathVariable("paletteId") String paletteId){
        paletteService.likePalette(getUserId(), paletteId);
        return new ResponseEntity<String>("Palette liked.", HttpStatus.OK);
    }

    @DeleteMapping("/palettes/{paletteId}/like")
    public ResponseEntity<String> unlikePalette(@PathVariable("paletteId") String paletteId){
        paletteService.unlikePalette(getUserId(), paletteId);
        return new ResponseEntity<String>("Palette unliked.", HttpStatus.OK);
    }

    private String getUserId(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
