package net.palettehub.api.palette.exception;

public class SortValueInvalidException extends RuntimeException{
    public SortValueInvalidException(String errMsg){
        super(errMsg);
    }
}