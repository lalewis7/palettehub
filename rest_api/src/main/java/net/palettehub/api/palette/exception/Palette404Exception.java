package net.palettehub.api.palette.exception;

/**
 * RuntimeException thrown when a palette could not be found.
 * 
 * @author Arthur Lewis
 */
public class Palette404Exception extends RuntimeException{
    public Palette404Exception(String errMsg){
        super(errMsg);
    }
}
