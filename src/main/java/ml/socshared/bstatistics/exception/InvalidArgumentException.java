package ml.socshared.bstatistics.exception;

import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends  AbstractRestHandleableException {
    public InvalidArgumentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
