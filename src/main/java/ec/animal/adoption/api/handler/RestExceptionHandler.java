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

import ec.animal.adoption.api.model.error.ApiErrorResponse;
import ec.animal.adoption.api.model.error.ApiSubErrorResponse;
import ec.animal.adoption.api.model.error.ValidationApiSubErrorResponse;
import ec.animal.adoption.domain.exception.*;
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

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String VALIDATION_FAILED_ERROR_MESSAGE = "Validation failed";

    @ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExistsException(final EntityAlreadyExistsException exception) {
        final HttpStatus conflict = HttpStatus.CONFLICT;
        Optional<Throwable> cause = Optional.ofNullable(exception.getCause());
        return buildResponseEntity(
                new ApiErrorResponse(
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
        return buildResponseEntity(new ApiErrorResponse(notFound, exception.getMessage()), notFound);
    }

    @ExceptionHandler(InvalidPictureException.class)
    public ResponseEntity<Object> handleInvalidPictureException(final InvalidPictureException exception) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Throwable cause = exception.getCause();
        ApiErrorResponse apiErrorResponse = cause == null ? new ApiErrorResponse(badRequest, exception.getMessage()) :
                new ApiErrorResponse(badRequest, exception.getMessage(), cause.getLocalizedMessage());

        return buildResponseEntity(apiErrorResponse, badRequest);
    }

    @ExceptionHandler(IllegalUpdateException.class)
    public ResponseEntity<Object> handleIllegalUpdateException(final IllegalUpdateException exception) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(new ApiErrorResponse(badRequest, exception.getMessage()), badRequest);
    }

    @ExceptionHandler(MediaStorageException.class)
    public ResponseEntity<Object> handleMediaStorageException(final MediaStorageException exception) {
        final HttpStatus serviceUnavailable = HttpStatus.SERVICE_UNAVAILABLE;
        return buildResponseEntity(new ApiErrorResponse(serviceUnavailable, exception.getMessage()), serviceUnavailable);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(final UnauthorizedException exception) {
        final HttpStatus forbidden = HttpStatus.FORBIDDEN;
        return buildResponseEntity(new ApiErrorResponse(forbidden, exception.getMessage()), forbidden);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(final Exception exception) {
        logger.error("Uncaught exception", exception);
        final HttpStatus serviceUnavailable = HttpStatus.SERVICE_UNAVAILABLE;
        return buildResponseEntity(new ApiErrorResponse(serviceUnavailable, exception.getMessage()), serviceUnavailable);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException exception,
                                                                  @Nullable final HttpHeaders headers,
                                                                  @Nullable final HttpStatus status,
                                                                  @Nullable final WebRequest request) {
        final String error = "Malformed JSON request";
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(new ApiErrorResponse(badRequest, error, exception.getMessage()), badRequest);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
                                                                  @Nullable final HttpHeaders headers,
                                                                  @Nullable final HttpStatus status,
                                                                  @Nullable final WebRequest request) {
        final List<ApiSubErrorResponse> apiSubErrorResponses = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> new ValidationApiSubErrorResponse(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(badRequest, VALIDATION_FAILED_ERROR_MESSAGE, exception.getMessage()).setSubErrors(apiSubErrorResponses);

        return buildResponseEntity(apiErrorResponse, badRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException exception) {
        final List<ApiSubErrorResponse> apiSubErrorResponses = exception.getConstraintViolations()
                .stream()
                .map(c -> new ValidationApiSubErrorResponse(c.getPropertyPath().toString(), c.getMessage()))
                .collect(Collectors.toList());
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(badRequest, VALIDATION_FAILED_ERROR_MESSAGE, exception.getMessage()).setSubErrors(apiSubErrorResponses);

        return buildResponseEntity(apiErrorResponse, badRequest);
    }

    private ResponseEntity<Object> buildResponseEntity(final ApiErrorResponse apiErrorResponse, final HttpStatus status) {
        return new ResponseEntity<>(apiErrorResponse, status);
    }
}
