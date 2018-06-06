package ec.animal.adoption.exceptions.handlers;

import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.models.rest.ApiError;
import ec.animal.adoption.models.rest.suberrors.ValidationError;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestExceptionHandlerTest {

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
        when(httpMessageNotReadableException.getLocalizedMessage()).thenReturn(debugMessage);
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
        EntityAlreadyExistsException entityAlreadyExistsException = mock(EntityAlreadyExistsException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityAlreadyExists(entityAlreadyExistsException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CONFLICT));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForEntityAlreadyExistsException() {
        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException();
        ApiError expectedApiError = new ApiError(HttpStatus.CONFLICT, "The resource already exists");

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
        when(methodArgumentNotValidException.getLocalizedMessage()).thenReturn(debugMessage);
        ApiError expectedApiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed", debugMessage)
                .setSubErrors(
                        fieldErrors.stream()
                        .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
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
    public void shouldReturnAResponseEntityWithHttpStatusUnprocessableEntityForImageProcessingException() {
        ImageProcessingException imageProcessingException = mock(ImageProcessingException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleImageProcessingError(
                imageProcessingException
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForImageProcessingException() {
        ImageProcessingException imageProcessingException = new ImageProcessingException();
        ApiError expectedApiError = new ApiError(
                HttpStatus.UNPROCESSABLE_ENTITY, "The image could not be processed"
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleImageProcessingError(
                imageProcessingException
        );

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError, is(expectedApiError));
    }

    private ArrayList<FieldError> createFieldErrors() {
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