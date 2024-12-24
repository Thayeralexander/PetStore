package pet.store.controller.error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Map;
import java.util.NoSuchElementException;
@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("Error: {}", ex.getMessage());
        return Map.of("message", ex.getMessage());
    }
}