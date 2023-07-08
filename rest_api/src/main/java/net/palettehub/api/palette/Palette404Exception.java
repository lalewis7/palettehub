package net.palettehub.api.palette;

public class Palette404Exception extends RuntimeException{
    public Palette404Exception(String errMsg){
        super(errMsg);
    }
}
