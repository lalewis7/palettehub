package net.palettehub.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.palettehub.api.palette.exception.PageValueInvalidException;
import net.palettehub.api.palette.exception.Palette404Exception;
import net.palettehub.api.palette.exception.PaletteLikeException;
import net.palettehub.api.palette.exception.SortValueInvalidException;
import net.palettehub.api.user.exception.GoogleAuthException;
import net.palettehub.api.user.exception.User404Exception;

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
    protected ResponseEntity<String> Palette404Invalid(Palette404Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaletteLikeException.class)
    protected ResponseEntity<String> PaletteLikeInvalid(PaletteLikeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GoogleAuthException.class)
    protected ResponseEntity<String> GoogleAuthInvalid(GoogleAuthException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(User404Exception.class)
    protected ResponseEntity<String> User404Invalid(User404Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
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
