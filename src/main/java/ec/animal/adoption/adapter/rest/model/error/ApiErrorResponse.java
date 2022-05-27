/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.adapter.rest.model.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiErrorResponse {

    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;

    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("debugMessage")
    private String debugMessage;

    @JsonProperty("subErrors")
    private List<ValidationApiSubErrorResponse> subErrors;

    private ApiErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(final HttpStatus status, final String message, final String debugMessage) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public ApiErrorResponse(final HttpStatus status, final String message) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = "";
    }

    public ApiErrorResponse setSubErrors(final List<ValidationApiSubErrorResponse> subErrors) {
        this.subErrors = subErrors;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) o;

        if (status != apiErrorResponse.status) {
            return false;
        }
        if (message != null ? !message.equals(apiErrorResponse.message) : apiErrorResponse.message != null) {
            return false;
        }
        if (debugMessage != null ? !debugMessage.equals(apiErrorResponse.debugMessage) : apiErrorResponse.debugMessage != null) {
            return false;
        }
        return subErrors != null ? subErrors.equals(apiErrorResponse.subErrors) : apiErrorResponse.subErrors == null;
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
