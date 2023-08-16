package net.palettehub.exception;

/**
 * RuntimeException thrown when a user tries to access private info of another user.
 * 
 * @author Arthur Lewis.
 */
public class RestrictedAccessException extends RuntimeException{
    public RestrictedAccessException(String errMsg){
        super(errMsg);
    }
}
