package net.palettehub.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.palettehub.api.palette.PageValueInvalidException;
import net.palettehub.api.palette.Palette404Exception;
import net.palettehub.api.palette.PaletteLikeException;
import net.palettehub.api.palette.SortValueInvalidException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SortValueInvalidException.class)
    protected ResponseEntity<String> sortValueInvalid(SortValueInvalidException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PageValueInvalidException.class)
    protected ResponseEntity<String> pageValueInvalid(PageValueInvalidException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Palette404Exception.class)
    protected ResponseEntity<String> pageValueInvalid(Palette404Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaletteLikeException.class)
    protected ResponseEntity<String> pageValueInvalid(PaletteLikeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // https://www.baeldung.com/spring-boot-bean-validation
    // https://stackoverflow.com/questions/51991992/getting-ambiguous-exceptionhandler-method-mapped-for-methodargumentnotvalidexce
    /*
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
     */

}
