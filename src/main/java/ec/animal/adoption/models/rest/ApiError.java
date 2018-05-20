package ec.animal.adoption.models.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.models.rest.suberrors.ApiSubError;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("debugMessage")
    private String debugMessage;

    @JsonProperty("subErrors")
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

    public ApiError setSubErrors(List<ApiSubError> subErrors) {
        this.subErrors = subErrors;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiError apiError = (ApiError) o;

        if (status != apiError.status) return false;
        if (message != null ? !message.equals(apiError.message) : apiError.message != null) return false;
        if (debugMessage != null ? !debugMessage.equals(apiError.debugMessage) : apiError.debugMessage != null)
            return false;
        return subErrors != null ? subErrors.equals(apiError.subErrors) : apiError.subErrors == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (debugMessage != null ? debugMessage.hashCode() : 0);
        result = 31 * result + (subErrors != null ? subErrors.hashCode() : 0);
        return result;
    }
}
