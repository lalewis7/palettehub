package net.palettehub.api.palette.exception;

public class PageValueInvalidException extends RuntimeException{
    public PageValueInvalidException(String errMsg){
        super(errMsg);
    }
}
