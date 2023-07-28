package net.palettehub.api.palette.exception;

/**
 * RuntimeException thrown when an invalid sort value is given.
 * 
 * @author Arthur Lewis
 */
public class SortValueInvalidException extends RuntimeException{
    public SortValueInvalidException(String errMsg){
        super(errMsg);
    }
}