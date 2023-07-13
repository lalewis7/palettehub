package net.palettehub.api.user;

public class GoogleAuthException extends RuntimeException{
    public GoogleAuthException(String errMsg){
        super(errMsg);
    }
}
