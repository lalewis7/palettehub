package net.palettehub.api.collection.exception;

public class Collection404Exception extends RuntimeException {
    public Collection404Exception(String errMsg){
        super(errMsg);
    }
}
