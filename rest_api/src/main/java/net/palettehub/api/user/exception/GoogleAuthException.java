package net.palettehub.api.user.exception;

public class GoogleAuthException extends RuntimeException{
    public GoogleAuthException(String errMsg){
        super(errMsg);
    }
}
