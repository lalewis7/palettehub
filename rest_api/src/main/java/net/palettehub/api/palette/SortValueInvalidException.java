package net.palettehub.api.palette;

public class SortValueInvalidException extends RuntimeException{
    public SortValueInvalidException(String errMsg){
        super(errMsg);
    }
}