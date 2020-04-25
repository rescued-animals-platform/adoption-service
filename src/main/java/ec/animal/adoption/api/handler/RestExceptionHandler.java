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

package ec.animal.adoption.api.handler;

import ec.animal.adoption.api.model.error.ApiError;
import ec.animal.adoption.api.model.error.ApiSubError;
import ec.animal.adoption.api.model.error.ValidationApiSubError;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.exception.InvalidPictureException;
import ec.animal.adoption.domain.exception.InvalidStateException;
import ec.animal.adoption.domain.exception.MediaStorageException;
import ec.animal.adoption.domain.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    public static final String VALIDATION_FAILED_ERROR_MESSAGE = "Validation failed";

    @ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExistsException(final EntityAlreadyExistsException exception) {
        final HttpStatus conflict = HttpStatus.CONFLICT;
        Optional<Throwable> cause = Optional.ofNullable(exception.getCause());
        return buildResponseEntity(
                new ApiError(
                        conflict,
                        exception.getMessage(),
                        cause.map(Throwable::getLocalizedMessage).orElse(null)
                ),
                conflict
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException exception) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        return buildResponseEntity(new ApiError(notFound, exception.getMessage()), notFound);
    }

    @ExceptionHandler(InvalidPictureException.class)
    public ResponseEntity<Object> handleInvalidPictureException(final InvalidPictureException exception) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Throwable cause = exception.getCause();
        ApiError apiError = cause == null ? new ApiError(badRequest, exception.getMessage()) :
                new ApiError(badRequest, exception.getMessage(), cause.getLocalizedMessage());

        return buildResponseEntity(apiError, badRequest);
    }

    @ExceptionHandler(MediaStorageException.class)
    public ResponseEntity<Object> handleMediaStorageException(final MediaStorageException exception) {
        final HttpStatus serviceUnavailable = HttpStatus.SERVICE_UNAVAILABLE;
        return buildResponseEntity(new ApiError(serviceUnavailable, exception.getMessage()), serviceUnavailable);
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<Object> handleInvalidStateException(final InvalidStateException exception) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(new ApiError(badRequest, exception.getMessage()), badRequest);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(final UnauthorizedException exception) {
        final HttpStatus forbidden = HttpStatus.FORBIDDEN;
        return buildResponseEntity(new ApiError(forbidden, exception.getMessage()), forbidden);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(final Exception exception) {
        LOGGER.error("Uncaught exception", exception);
        final HttpStatus serviceUnavailable = HttpStatus.SERVICE_UNAVAILABLE;
        return buildResponseEntity(new ApiError(serviceUnavailable, exception.getMessage()), serviceUnavailable);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException exception,
            @Nullable final HttpHeaders headers,
            @Nullable final HttpStatus status,
            @Nullable final WebRequest request
    ) {
        final String error = "Malformed JSON request";
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(new ApiError(badRequest, error, exception.getMessage()), badRequest);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception,
            @Nullable final HttpHeaders headers,
            @Nullable final HttpStatus status,
            @Nullable final WebRequest request
    ) {
        final List<ApiSubError> apiSubErrors = exception.getBindingResult().getFieldErrors()
                                                        .stream()
                                                        .map(f -> new ValidationApiSubError(f.getField(), f.getDefaultMessage()))
                                                        .collect(Collectors.toList());
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(badRequest, VALIDATION_FAILED_ERROR_MESSAGE, exception.getMessage()).setSubErrors(apiSubErrors);

        return buildResponseEntity(apiError, badRequest);
    }

    private ResponseEntity<Object> buildResponseEntity(final ApiError apiError, final HttpStatus status) {
        return new ResponseEntity<>(apiError, status);
    }
}
