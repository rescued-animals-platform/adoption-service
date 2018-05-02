package ec.animal.adoption.exceptions.handlers;

import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestExceptionHandlerTest {

    private RestExceptionHandler restExceptionHandler;

    @Before
    public void setUp() {
        restExceptionHandler = new RestExceptionHandler();
    }

    @Test
    public void shouldBeAnInstanceOfResponseEntityExceptionHandler() {
        assertThat(restExceptionHandler, is(instanceOf(ResponseEntityExceptionHandler.class)));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusBadRequestForHttpMessageNotReadableException() {
        HttpMessageNotReadableException httpMessageNotReadableException = mock(HttpMessageNotReadableException.class);
        WebRequest webRequest = mock(WebRequest.class);
        HttpHeaders headers = mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.MULTI_STATUS;

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
        HttpHeaders headers = mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.MULTI_STATUS;
        WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleHttpMessageNotReadable(
                httpMessageNotReadableException, headers, status, webRequest
        );

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError.getStatus(), is(HttpStatus.BAD_REQUEST));
        assertThat(apiError.getMessage(), is("Malformed JSON request"));
        assertThat(apiError.getDebugMessage(), is(debugMessage));
    }

    @Test
    public void shouldReturnAResponseEntityWithHttpStatusConflictForEntityAlreadyExistsException() {
        EntityAlreadyExistsException entityAlreadyExistsException = mock(EntityAlreadyExistsException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityNotFound(entityAlreadyExistsException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CONFLICT));
    }

    @Test
    public void shouldReturnAResponseEntityWithApiErrorForEntityAlreadyExistsException() {
        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException();

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityNotFound(entityAlreadyExistsException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiError.class)));
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertThat(apiError.getStatus(), is(HttpStatus.CONFLICT));
        assertThat(apiError.getMessage(), is("The resource already exists"));
        assertThat(apiError.getDebugMessage(), is(""));
    }
}