package net.palettehub.api.palette;

import java.util.List;

/**
 * List of palettes model.
 * 
 * @author Arthur Lewis
 */
public class PaletteList {
    private List<Palette> palettes;
    private int count;

    public PaletteList() {}

    public PaletteList(List<Palette> palettes, int count) {
        this.palettes = palettes;
        this.count = count;
    }

    public List<Palette> getPalettes() {
        return this.palettes;
    }

    public void setPalettes(List<Palette> palettes) {
        this.palettes = palettes;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "{" +
            " palettes='" + getPalettes() + "'" +
            ", count='" + getCount() + "'" +
            "}";
    }

}
