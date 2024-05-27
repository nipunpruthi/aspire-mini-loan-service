package learn.nipun.aspireminiloanservice.exception;

import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE = "message";

    @ExceptionHandler(value = ResourceAccessForbidden.class)
    public ResponseEntity<Object> handleForbidden(Exception e) {

        Map<String, Object> body = new HashMap<>();
        body.put(MESSAGE, e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(Exception e) {

        Map<String, Object> body = new HashMap<>();
        body.put(MESSAGE, e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidStatusException.class)
    public ResponseEntity<Object> handleInvalidAction(Exception e) {

        Map<String, Object> body = new HashMap<>();
        body.put(MESSAGE, e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<Object> handleValidationException(Exception e) {

        Map<String, Object> body = new HashMap<>();
        body.put(MESSAGE, e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }
}
