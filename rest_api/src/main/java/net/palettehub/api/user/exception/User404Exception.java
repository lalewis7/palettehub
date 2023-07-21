package net.palettehub.api.user.exception;

public class User404Exception extends RuntimeException {
    public User404Exception(String errMsg){
        super(errMsg);
    }
}
