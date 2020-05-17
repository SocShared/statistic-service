package ml.socshared.bstatistics.exception;

import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends  AbstractRestHandleableException {
    public InvalidArgumentException(String message) {
        super(message, SocsharedErrors.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }
}
