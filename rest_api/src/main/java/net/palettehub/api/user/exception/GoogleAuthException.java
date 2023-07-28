package net.palettehub.api.user.exception;

/**
 * RuntimeException thrown when an issue arrises with google sign in.
 * 
 * @author Arthur Lewis
 */
public class GoogleAuthException extends RuntimeException{
    public GoogleAuthException(String errMsg){
        super(errMsg);
    }
}
