package net.palettehub.api.palette;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PaletteService {

    @Autowired
    private PaletteRepository paletteRepository;

    @Autowired
    @PersistenceContext
    private EntityManager em;
    
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
        Palette palette = paletteRepository.getPaletteById(userId, paletteId);
        // 404 error
        if (palette == null)
            throw new Palette404Exception("Palette not found.");
        else
            return palette;
    }

    public void likePalette(String userId, String paletteId){
        Palette palette = paletteRepository.getPaletteById(userId, paletteId);
        // 404 error
        if (palette == null)
            throw new Palette404Exception("Palette not found.");
        // palette already liked if false
        if (!paletteRepository.likePalette(userId, paletteId))
            throw new PaletteLikeException("Palette already liked.");
    }

    public void unlikePalette(String userId, String paletteId){
        Palette palette = paletteRepository.getPaletteById(userId, paletteId);
        // 404 error
        if (palette == null)
            throw new Palette404Exception("Palette not found.");
        // palette already liked if false
        if (!paletteRepository.unlikePalette(userId, paletteId))
            throw new PaletteLikeException("Palette not liked.");
    }
}
