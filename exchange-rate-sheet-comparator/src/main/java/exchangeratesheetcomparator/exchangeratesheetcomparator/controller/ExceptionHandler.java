package exchangeratesheetcomparator.exchangeratesheetcomparator.controller;

import exchangeratesheetcomparator.exchangeratesheetcomparator.exception.InvalidCurrencyPairException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidCurrencyPairException.class)
    public ResponseEntity<ApiError> handleInvalidCurrencyException(InvalidCurrencyPairException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ApiError("InvalidCurrencyPair", e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleException(Throwable t) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(new ApiError("InternalServerError", t.getMessage()));
    }
    
    public record ApiError(
            String code,
            String message) {
    }

}
