package ml.socshared.bstatistics.exception;

import org.springframework.http.HttpStatus;

public interface HttpStatusCodeContainer {
    HttpStatus getHttpStatus();
}
