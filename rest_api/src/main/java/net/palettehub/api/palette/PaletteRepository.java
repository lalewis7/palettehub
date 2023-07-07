package net.palettehub.api.palette;

import java.util.List;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.Repository;

public interface PaletteRepository extends Repository<Palette, String>{
    
    // find new palettes
    @Procedure("get_new_palettes")
    List<Palette> getNewPalettes(String username, int page);

    // find popular palettes
    @Procedure("get_popular_palettes")
    List<Palette> getPopularPalettes(String username, int page);

    // get palette by id
    @Procedure("get_palette_by_id")
    Palette getPaletteById(String paletteId, String userId);

    // create a palette
    @Procedure("create_palette")
    void createPalette(String paletteId, String userId, String color1, String color2, String color3, String color4, String color5, long posted);

    // like a palette
    @Procedure("like_palette")
    int likePalette(String paletteId, String userId);

    // unlike a palette
    @Procedure("unlike_palette")
    int unlikePalette(String paletteId, String userId);
}
