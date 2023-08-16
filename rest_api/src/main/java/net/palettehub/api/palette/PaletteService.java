package net.palettehub.api.palette;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.palettehub.api.palette.exception.PageValueInvalidException;
import net.palettehub.api.palette.exception.Palette404Exception;
import net.palettehub.api.palette.exception.PaletteLikeException;
import net.palettehub.api.palette.exception.SortValueInvalidException;
import net.palettehub.exception.RestrictedAccessException;

/**
 * Palette Service containing business code of the API for palettes.
 * 
 * @author Arthur Lewis
 */
@Service
public class PaletteService {

    @Autowired
    private PaletteRepository paletteRepository;
    
    public PaletteList getPalettes(String userId, String sort, String page){
        // Check sort value
        if ((!sort.equals("new") && !sort.equals("popular")) || sort == null)
            throw new SortValueInvalidException("Invalid sort value, must be \"new\" or \"popular\".");

        // check page value
        int pageValue;
        if (page.equals("")){ // no page given set to 1
            pageValue = 1;
        }
        else { // check passed page value
            try {pageValue = Integer.parseInt(page);}
            catch (NumberFormatException e) {throw new PageValueInvalidException("Page value invalid.");}
        }

        // new vs popular page
        if (sort.toLowerCase().equals("new"))
            return paletteRepository.getNewPalettes(userId, pageValue);
        else if (sort.toLowerCase().equals("popular"))
            return paletteRepository.getPopularPalettes(userId, pageValue);
        else
            throw new SortValueInvalidException("Invalid sort value, must be \"new\" or \"popular\".");
    }

    public String createPalette(String userId, Palette palette){
        palette.setUserId(userId);
        return paletteRepository.createPalette(palette);
    }

    public Palette getPalette(String userId, String paletteId){
        // not uuid
        if (paletteId.length() != 32)
            throw new Palette404Exception("Palette not found.");
        Palette palette = paletteRepository.getPaletteById(userId, paletteId);
        // 404 error
        if (palette == null)
            throw new Palette404Exception("Palette not found.");
        else
            return palette;
    }

    public void likePalette(String userId, String paletteId){
        // not uuid
        if (paletteId.length() != 32)
            throw new Palette404Exception("Palette not found.");
        Palette palette = paletteRepository.getPaletteById(userId, paletteId);
        // 404 error
        if (palette == null)
            throw new Palette404Exception("Palette not found.");
        // palette already liked if false
        if (!paletteRepository.likePalette(userId, paletteId))
            throw new PaletteLikeException("Palette already liked.");
    }

    public void unlikePalette(String userId, String paletteId){
        // not uuid
        if (paletteId.length() != 32)
            throw new Palette404Exception("Palette not found.");
        Palette palette = paletteRepository.getPaletteById(userId, paletteId);
        // 404 error
        if (palette == null)
            throw new Palette404Exception("Palette not found.");
        // palette already liked if false
        if (!paletteRepository.unlikePalette(userId, paletteId))
            throw new PaletteLikeException("Palette not liked.");
    }

    public void deletePalette(String paletteId){
        Palette palette = getPalette("", paletteId);
        if (palette.getUserId().equals(getUserId()) || hasAuthority("ROLE_ADMIN") || hasAuthority("ROLE_MODE"))
            paletteRepository.deletePalette(paletteId);
        else
            throw new RestrictedAccessException("You do not have access to delete this palette.");
    }

    private boolean hasAuthority(Object authority){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(authority);
    }

    private String getUserId(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
