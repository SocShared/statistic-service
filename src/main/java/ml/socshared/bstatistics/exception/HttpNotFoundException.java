package ml.socshared.bstatistics.exception;

import org.springframework.http.HttpStatus;

public class HttpNotFoundException extends AbstractRestHandleableException {
    public HttpNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
