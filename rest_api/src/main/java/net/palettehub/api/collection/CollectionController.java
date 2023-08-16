package net.palettehub.api.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Rest Controller for Collection routes.
 * 
 * @author Arthur Lewis
 */
@RestController
public class CollectionController {
    
    @Autowired
    private CollectionService collectionService;

    // create collection
    @PostMapping("/collections")
    public ResponseEntity<String> postCollection(@Valid @RequestBody Collection collection){
        return new ResponseEntity<String>(collectionService.createCollection(getUserId(), collection), HttpStatus.CREATED);
    }

    // get collection
    @GetMapping("/collections/{collectionId}")
    public ResponseEntity<Collection> getCollection(@PathVariable("collectionId") String collectionId){
        return new ResponseEntity<Collection>(collectionService.getCollection(collectionId), HttpStatus.OK);
    }

    // edit collection
    @PutMapping("/collections/{collectionId}")
    public ResponseEntity<String> putCollection(@PathVariable("collectionId") String collectionId, @Valid @RequestBody Collection collection){
        collectionService.editCollection(collectionId, collection);
        return new ResponseEntity<String>("Changes saved.", HttpStatus.OK);
    }

    // delete collection
    @DeleteMapping("/collections/{collectionId}")
    public ResponseEntity<String> deleteCollection(@PathVariable("collectionId") String collectionId){
        collectionService.deleteCollection(collectionId);
        return new ResponseEntity<String>("Collection deleted.", HttpStatus.OK);
    }

    // add palette to 
    @PostMapping("/collections/{collectionId}/palettes/{paletteId}")
    public ResponseEntity<String> addToCollection(@PathVariable("collectionId") String collectionId, @PathVariable("paletteId") String paletteId){
        collectionService.addToCollection(collectionId, paletteId);
        return new ResponseEntity<String>("Palette added to collection.", HttpStatus.OK);
    }

    // removing palette from collection
    @DeleteMapping("/collections/{collectionId}/palettes/{paletteId}")
    public ResponseEntity<String> removeFromCollection(@PathVariable("collectionId") String collectionId, @PathVariable("paletteId") String paletteId){
        collectionService.removeFromCollection(collectionId, paletteId);
        return new ResponseEntity<String>("Palette removed from collection.", HttpStatus.OK);
    }

    private String getUserId(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
