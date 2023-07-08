package net.palettehub.api.palette;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class PaletteController {

    @Autowired
    private PaletteService paletteService;
    
    @GetMapping("/palettes")
    public ResponseEntity<PaletteList> getPalettes(@RequestParam("sort") String sort, @RequestParam("page") String page) {
        // TODO: Check JWT
        return new ResponseEntity<PaletteList>(paletteService.getPalettes("", sort, page), HttpStatus.OK);
    }

    @PostMapping("/palettes")
    public ResponseEntity<String> postPalette(@Valid @RequestBody Palette palette){
        // TODO: Check JWT
        return new ResponseEntity<String>(paletteService.createPalette("", palette), HttpStatus.CREATED);
    }

    @GetMapping("/palettes/{paletteId}")
    public ResponseEntity<Palette> getPalette(@PathVariable("paletteId") String paletteId){
        // TODO: Check JWT
        return new ResponseEntity<Palette>(paletteService.getPalette("", paletteId), HttpStatus.OK);
    }

    @PostMapping("/palettes/{palettedId}/like")
    public ResponseEntity<String> likePalette(@PathVariable("paletteId") String paletteId){
        // TODO: Check JWT
        paletteService.likePalette("", paletteId);
        return new ResponseEntity<String>("Palette liked.", HttpStatus.OK);
    }

    @DeleteMapping("/palettes/{palettedId}/like")
    public ResponseEntity<String> unlikePalette(@PathVariable("paletteId") String paletteId){
        // TODO: Check JWT
        paletteService.unlikePalette("", paletteId);
        return new ResponseEntity<String>("Palette unliked.", HttpStatus.OK);
    }

}
