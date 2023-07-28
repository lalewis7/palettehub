package net.palettehub.api.user.exception;

/**
 * RuntimeException thrown when a user cannot be found.
 * 
 * @author Arthur Lewis
 */
public class User404Exception extends RuntimeException {
    public User404Exception(String errMsg){
        super(errMsg);
    }
}
