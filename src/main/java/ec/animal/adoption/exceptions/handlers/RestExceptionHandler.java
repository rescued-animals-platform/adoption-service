package ec.animal.adoption.exceptions.handlers;

import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.exceptions.ImageStorageException;
import ec.animal.adoption.models.rest.ApiError;
import ec.animal.adoption.models.rest.suberrors.ApiSubError;
import ec.animal.adoption.models.rest.suberrors.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        final String error = "Malformed JSON request";
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(new ApiError(badRequest, error, ex.getLocalizedMessage()), badRequest);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        return buildResponseEntity(new ApiError(conflict, ex.getMessage()), conflict);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return buildResponseEntity(new ApiError(notFound, ex.getMessage()), notFound);
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<Object> handleImageMediaProcessingError(ImageProcessingException ex) {
        HttpStatus unprocessableEntity = HttpStatus.UNPROCESSABLE_ENTITY;
        return buildResponseEntity(new ApiError(unprocessableEntity, ex.getMessage()), unprocessableEntity);
    }

    @ExceptionHandler(ImageStorageException.class)
    public ResponseEntity<Object> handleImageStorageException(ImageStorageException ex) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        return buildResponseEntity(new ApiError(internalServerError, ex.getMessage()), internalServerError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        final String error = "Validation failed";
        List<ApiSubError> apiSubErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> new ValidationError(f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(
                new ApiError(badRequest, error, ex.getLocalizedMessage()).setSubErrors(apiSubErrors), badRequest
        );
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus status) {
        return new ResponseEntity<>(apiError, status);
    }
}
