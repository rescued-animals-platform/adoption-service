package ec.animal.adoption.models.rest;

import ec.animal.adoption.models.rest.suberrors.ApiSubError;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ApiError implements Serializable {

    private final LocalDateTime timestamp;
    private String debugMessage;
    private HttpStatus status;
    private String message;
    private List<ApiSubError> subErrors;

    private ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message, String debugMessage) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public ApiError(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = "";
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ApiError setSubErrors(List<ApiSubError> subErrors) {
        this.subErrors = subErrors;
        return this;
    }

    public List<ApiSubError> getSubErrors() {
        return subErrors;
    }
}
