package net.palettehub.api.palette.exception;

/**
 * RuntimeException thrown when a palette could not be properly liked most likely because the user
 * is trying to like a palette they've already liked or unlike something they have not liked.
 * 
 * @author Arthur Lewis
 */
public class PaletteLikeException extends RuntimeException{
    public PaletteLikeException(String errMsg){
        super(errMsg);
    }
}
