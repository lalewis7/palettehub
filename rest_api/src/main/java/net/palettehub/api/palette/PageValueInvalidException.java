package net.palettehub.api.palette;

public class PageValueInvalidException extends RuntimeException{
    public PageValueInvalidException(String errMsg){
        super(errMsg);
    }
}
