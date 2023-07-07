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
    
    // get new/popular palettes
    // public List getNewPalettes(){}

    // public List getPopularPalettes(){}

    // get individual palette

    // post a palette

    // like a palette

    // unlike a palettes

}
