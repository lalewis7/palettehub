package net.palettehub.api.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.palettehub.api.collection.exception.Collection404Exception;
import net.palettehub.api.palette.PaletteList;
import net.palettehub.api.palette.PaletteService;
import net.palettehub.api.palette.exception.PageValueInvalidException;
import net.palettehub.exception.RestrictedAccessException;

@Service
public class CollectionService {
    
    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private PaletteService paletteService;

    // POST	/collections/
    public String createCollection(String userId, Collection collection){
        collection.setUserId(userId);
        return collectionRepository.createCollection(collection);
    }

    private Collection getCollection(String collectionId){
        // not uuid
        if (collectionId.length() != 32)
            throw new Collection404Exception("Collection not found.");
        Collection collection = collectionRepository.getCollection(collectionId);
        // 404 error
        if (collection == null)
            throw new Collection404Exception("Collection not found.");
        else
            return collection;
    }

    // GET	/collections/:collectionId
    public Collection getCollection(String collectionId, String page){
        // check page value
        int pageValue;
        if (page.equals("")){ // no page given set to 1
            pageValue = 1;
        }
        else { // check passed page value
            try {pageValue = Integer.parseInt(page);}
            catch (NumberFormatException e) {throw new PageValueInvalidException("Page value invalid.");}
        }
        Collection collection = getCollection(collectionId);
        PaletteList paletteList = collectionRepository.getCollectionPalettes(collectionId, getUserId(), pageValue);
        collection.setCount(paletteList.getCount());
        collection.setPalettes(paletteList.getPalettes());
        return collection;
    }

    // PUT	/collections/:collectionId
    public void editCollection(String collectionId, Collection newCollection){
        Collection collection = getCollection(collectionId);
        // owner or admin
        if (collection.getUserId().equals(getUserId()) || hasAuthority("ROLE_ADMIN")){
            collectionRepository.editCollection(collectionId, newCollection);
        }
        // does not have access
        else 
            throw new RestrictedAccessException("You do not have access to edit this collection.");
    }

    // DELETE	/collections/:collectionId
    public void deleteCollection(String collectionId){
        Collection collection = getCollection(collectionId);
        // owner, mod, or admin
        if (collection.getUserId().equals(getUserId())
                || hasAuthority("ROLE_ADMIN")
                || hasAuthority("ROLE_MOD"))
            collectionRepository.deleteCollection(collectionId);
        else
            throw new RestrictedAccessException("You do not have access to delete this collection.");
    }

    // POST	/collections/:collectionId/palettes/:paletteId
    public void addToCollection(String collectionId, String paletteId){
        Collection collection = getCollection(collectionId);
        paletteService.getPalette("", paletteId);
        if (collection.getUserId().equals(getUserId()))
            collectionRepository.addToCollection(collectionId, paletteId);
        else
            throw new RestrictedAccessException("You do not have access to add palettes to this collection.");
    }

    // DELETE	/collections/:collectionId/palettes/:paletteId
    public void removeFromCollection(String collectionId, String paletteId){
        Collection collection = getCollection(collectionId);
        paletteService.getPalette("", paletteId);
        if (collection.getUserId().equals(getUserId()))
            collectionRepository.removeFromCollection(collectionId, paletteId);
        else
            throw new RestrictedAccessException("You do not have access to remove palettes from this collection.");
    }

    private String getUserId(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean hasAuthority(String authority){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(authority));
    }

}
