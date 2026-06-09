package itt.marconi.fantacatalogo.exceptions;

import itt.marconi.fantacatalogo.dtos.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. (400) Bad Request "fail"
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        APIResponse<Map<String, String>> response = APIResponse.<Map<String, String>>builder()
                .status("fail")
                .data(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 2.(404) Not Found "fail"
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<APIResponse<Void>> handleResponseStatusException(ResponseStatusException ex) {
        APIResponse<Void> response = APIResponse.<Void>builder()
                .status("fail")
                .message(ex.getReason())
                .build();
        
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    // 3. (500) Server Error "error"
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleGenericException(Exception ex) {
        APIResponse<Void> response = APIResponse.<Void>builder()
                .status("error")
                .message("Si è verificato un errore interno al server.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}