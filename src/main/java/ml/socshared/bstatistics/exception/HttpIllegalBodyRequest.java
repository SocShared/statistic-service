package ml.socshared.bstatistics.exception;

import org.springframework.http.HttpStatus;

public class HttpIllegalBodyRequest extends AbstractRestHandleableException {
    public HttpIllegalBodyRequest(String message) {
        super(message, SocsharedErrors.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }
}
