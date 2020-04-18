package ec.animal.adoption.api.handler;

import ec.animal.adoption.api.model.error.ApiError;
import ec.animal.adoption.api.model.error.ValidationApiSubError;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.exception.ImageStorageException;
import ec.animal.adoption.domain.exception.InvalidPictureException;
import ec.animal.adoption.domain.exception.InvalidStateException;
import ec.animal.adoption.domain.exception.MediaStorageException;
import ec.animal.adoption.domain.media.SupportedImageExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings({"PMD.TooManyMethods", "PMD.ExcessiveImports"})
public class RestExceptionHandlerTest {

    private static final String INVALID_PICTURE_EXCEPTION_MESSAGE = "The image(s) could not be processed. Check they meet the " +
            "minimum requirements: Supported extensions: %s. Maximum size: 1MB. " +
            "Only PRIMARY picture type is supported.";

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpHeaders headers;

    private HttpStatus status;
    private RestExceptionHandler restExceptionHandler;

    @Before
    public void setUp() {
        status = HttpStatus.MULTI_STATUS;
        restExceptionHandler = new RestExceptionHandler();
    }

    @Test
    public void shouldBeAnInstanceOfResponseEntityExceptionHandler() {
        assertThat(restExceptionHandler, is(instanceOf(ResponseEntityExceptionHandler.class)));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusBadRequestForHttpMessageNotReadableException() {
        HttpMessageNotReadableException httpMessageNotReadableException = mock(HttpMessageNotReadableException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleHttpMessageNotReadable(
                httpMessageNotReadableException, headers, status, webRequest
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForHttpMessageNotReadableException() {
        HttpMessageNotReadableException httpMessageNotReadableException = mock(HttpMessageNotReadableException.class);
        String debugMessage = randomAlphabetic(10);
        when(httpMessageNotReadableException.getMessage()).thenReturn(debugMessage);
        ApiError expectedApiError = new ApiError(
                HttpStatus.BAD_REQUEST, "Malformed JSON request", debugMessage
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleHttpMessageNotReadable(
                httpMessageNotReadableException, headers, status, webRequest
        );

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusConflictForEntityAlreadyExistsException() {
        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException(
                mock(Throwable.class)
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityAlreadyExists(
                entityAlreadyExistsException
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CONFLICT));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForEntityAlreadyExistsException() {
        Throwable throwable = mock(Throwable.class);
        String localizedMessage = randomAlphabetic(10);
        when(throwable.getLocalizedMessage()).thenReturn(localizedMessage);
        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException(throwable);
        ApiError expectedApiError = new ApiError(
                HttpStatus.CONFLICT, "The resource already exists", localizedMessage
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityAlreadyExists(entityAlreadyExistsException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusBadRequestForMethodArgumentNotValidException() {
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleMethodArgumentNotValid(
                methodArgumentNotValidException, headers, status, webRequest
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForMethodArgumentNotValidException() {
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        List<FieldError> fieldErrors = createFieldErrors();
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        String debugMessage = randomAlphabetic(10);
        when(methodArgumentNotValidException.getMessage()).thenReturn(debugMessage);
        ApiError expectedApiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed", debugMessage)
                .setSubErrors(
                        fieldErrors.stream()
                                   .map(fieldError -> new ValidationApiSubError(fieldError.getField(), fieldError.getDefaultMessage()))
                                   .collect(Collectors.toList())
                );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleMethodArgumentNotValid(
                methodArgumentNotValidException, headers, status, webRequest
        );

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusNotFoundForEntityNotFoundException() {
        EntityNotFoundException entityNoyFoundException = mock(EntityNotFoundException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityNotFound(entityNoyFoundException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForEntityNotFoundException() {
        EntityNotFoundException entityNotFoundException = new EntityNotFoundException();
        ApiError expectedApiError = new ApiError(HttpStatus.NOT_FOUND, "Unable to find the resource");

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityNotFound(entityNotFoundException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusBadRequestForInvalidPictureException() {
        InvalidPictureException invalidPictureException = mock(InvalidPictureException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleImageMediaProcessingError(
                invalidPictureException
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForInvalidPictureException() {
        InvalidPictureException invalidPictureException = new InvalidPictureException();
        ApiError expectedApiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                String.format(INVALID_PICTURE_EXCEPTION_MESSAGE, Arrays.stream(SupportedImageExtension.values()).
                        map(SupportedImageExtension::getExtension).
                                                                               collect(Collectors.joining(", ")))
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleImageMediaProcessingError(
                invalidPictureException
        );

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForInvalidPictureExceptionWithThrowableCause() {
        Throwable throwable = mock(Throwable.class);
        String localizedMessage = randomAlphabetic(10);
        when(throwable.getLocalizedMessage()).thenReturn(localizedMessage);
        InvalidPictureException invalidPictureException = new InvalidPictureException(throwable);
        ApiError expectedApiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                String.format(INVALID_PICTURE_EXCEPTION_MESSAGE, Arrays.stream(SupportedImageExtension.values()).
                        map(SupportedImageExtension::getExtension).
                                                                               collect(Collectors.joining(", "))),
                localizedMessage
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleImageMediaProcessingError(
                invalidPictureException
        );

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusInternalServerErrorForImageStorageException() {
        ImageStorageException imageStorageException = mock(ImageStorageException.class);
        when(imageStorageException.getCause()).thenReturn(mock(Throwable.class));

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleImageStorageException(imageStorageException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForImageStorageException() {
        Throwable throwable = mock(Throwable.class);
        String localizedMessage = randomAlphabetic(10);
        when(throwable.getLocalizedMessage()).thenReturn(localizedMessage);
        ImageStorageException imageStorageException = new ImageStorageException(throwable);
        ApiError expectedApiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, "The image could not be stored", localizedMessage
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleImageStorageException(imageStorageException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusServiceUnavailableErrorForMediaStorageException() {
        MediaStorageException mediaStorageException = mock(MediaStorageException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleMediaStorageException(mediaStorageException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.SERVICE_UNAVAILABLE));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForMediaStorageException() {
        MediaStorageException mediaStorageException = new MediaStorageException();
        ApiError expectedApiError = new ApiError(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Can not fulfill the request now. Please, retry later (client unavailable)"
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleMediaStorageException(mediaStorageException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusBadRequestForInvalidStateException() {
        InvalidStateException invalidStateException = mock(InvalidStateException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleInvalidStateError(invalidStateException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForInvalidStateException() {
        String stateName = randomAlphabetic(10);
        String invalidMessage = String.format("%s is not a valid state", stateName);
        InvalidStateException invalidStateException = new InvalidStateException(stateName);
        ApiError expectedApiError = new ApiError(HttpStatus.BAD_REQUEST, invalidMessage);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleInvalidStateError(invalidStateException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    private List<FieldError> createFieldErrors() {
        ArrayList<FieldError> fieldErrors = new ArrayList<>();
        FieldError fieldError = new FieldError(
                randomAlphabetic(10), randomAlphabetic(10), randomAlphabetic(10)
        );
        FieldError anotherFieldError = new FieldError(
                randomAlphabetic(10), randomAlphabetic(10), randomAlphabetic(10)
        );
        fieldErrors.add(fieldError);
        fieldErrors.add(anotherFieldError);
        return fieldErrors;
    }
}