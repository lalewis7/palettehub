package net.palettehub.api.palette;

import org.springframework.stereotype.Repository;

/**
 * Palette Repository interface.
 * 
 * @author Arthur Lewis
 */
@Repository
public interface PaletteRepository {
    
    // find new palettes
    PaletteList getNewPalettes(String userId, int page);

    // find popular palettes
    PaletteList getPopularPalettes(String userId, int page);

    // get palette by id
    Palette getPaletteById(String userId, String paletteId);

    // create a palette
    String createPalette(Palette palette);

    // like a palette
    boolean likePalette(String userId, String paletteId);

    // unlike a palette
    boolean unlikePalette(String userId, String paletteId);
    
    // delete a palette
    boolean deletePalette(String paletteId);

}
