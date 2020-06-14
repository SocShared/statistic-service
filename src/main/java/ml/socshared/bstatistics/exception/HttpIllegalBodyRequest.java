package ml.socshared.bstatistics.exception;

import org.springframework.http.HttpStatus;

public class HttpIllegalBodyRequest extends AbstractRestHandleableException {
    public HttpIllegalBodyRequest(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
