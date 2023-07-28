package net.palettehub.api.palette.exception;

/**
 * RuntimeException thrown when an invalid page query value is given on a request.
 * 
 * @author Arthur Lewis
 */
public class PageValueInvalidException extends RuntimeException{
    public PageValueInvalidException(String errMsg){
        super(errMsg);
    }
}
