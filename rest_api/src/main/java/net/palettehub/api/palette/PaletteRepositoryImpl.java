package net.palettehub.api.palette;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

/**
 * Implementation of palette repository instance. Calls mysql sprocs.
 * 
 * @author Arthur Lewis
 * @see PaletteRepository
 */
@Repository
public class PaletteRepositoryImpl implements PaletteRepository {

    public static final int PAGE_SIZE = 50;
    
    @Autowired
    private EntityManager em;

    public static PaletteList getPalettes(String userId, int page, String sproc, EntityManager em){
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery(sproc, Palette.class);
        
        // register params
        query.registerStoredProcedureParameter("userId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pageSize", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("pageNum", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("count", Integer.class, ParameterMode.OUT);

        // set params
        query.setParameter("userId", userId);
        query.setParameter("pageSize", PAGE_SIZE);
        query.setParameter("pageNum", page);

        // get output param count
        int count = ((Number) query.getOutputParameterValue("count")).intValue();

        // get result list
        @SuppressWarnings("unchecked")
        List<Palette> palettes = query.getResultList();

        // return palette list
        return new PaletteList(palettes, count);
    }

    @Override
    public PaletteList getNewPalettes(String userId, int page){
        return getPalettes(userId, page, "find_new_palettes", em);
    }

    @Override
    public PaletteList getPopularPalettes(String userId, int page){
        return getPalettes(userId, page, "find_popular_palettes", em);
    }

    @Override
    public Palette getPaletteById(String userId, String paletteId){
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery("get_palette_by_id", Palette.class);

        // register params
        query.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("userId", String.class, ParameterMode.IN);

        // set params
        query.setParameter("id", paletteId);
        query.setParameter("userId", userId);

        // get result
        return (Palette) query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public String createPalette(Palette palette){
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery("create_palette", Palette.class);

        // register params
        query.registerStoredProcedureParameter("userId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("color1", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("color2", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("color3", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("color4", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("color5", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("paletteId", String.class, ParameterMode.OUT);

        // set params
        query.setParameter("userId", palette.getUserId());
        query.setParameter("color1", palette.getColor1());
        query.setParameter("color2", palette.getColor2());
        query.setParameter("color3", palette.getColor3());
        query.setParameter("color4", palette.getColor4());
        query.setParameter("color5", palette.getColor5());

        // return new id
        return (String) query.getOutputParameterValue("paletteId");
    }

    private boolean likeSproc(String userId, String paletteId, String sproc){
        // create sproc query
        StoredProcedureQuery query = em.createStoredProcedureQuery(sproc, Palette.class);

        // register params
        query.registerStoredProcedureParameter("paletteId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("userId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", Integer.class, ParameterMode.OUT);

        // set params
        query.setParameter("paletteId", paletteId);
        query.setParameter("userId", userId);

        // 1 - success, 0 - failure
        return ((Number) query.getOutputParameterValue("result")).intValue() == 1;
    }

    @Override
    public boolean likePalette(String userId, String paletteId){
        return likeSproc(userId, paletteId, "like_palette");
    }

    @Override
    public boolean unlikePalette(String userId, String paletteId){
        return likeSproc(userId, paletteId, "unlike_palette");
    }

    @Override
    public boolean deletePalette(String paletteId){
        StoredProcedureQuery query = em.createStoredProcedureQuery("delete_palette", Palette.class);

        query.registerStoredProcedureParameter("paletteId", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result", Integer.class, ParameterMode.OUT);

        query.setParameter("paletteId", paletteId);

        return ((Number) query.getOutputParameterValue("result")).intValue() > 0;
    }

}
