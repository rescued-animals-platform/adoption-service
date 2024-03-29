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

package ec.animal.adoption.adapter.rest.handler;

import ec.animal.adoption.adapter.rest.model.error.ApiErrorResponse;
import ec.animal.adoption.adapter.rest.model.error.ValidationApiSubErrorResponse;
import ec.animal.adoption.domain.model.exception.*;
import ec.animal.adoption.domain.model.media.SupportedImageExtension;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    private static final String INVALID_PICTURE_EXCEPTION_MESSAGE = "The image(s) could not be processed. Check they meet the " +
                                                                    "minimum requirements: Supported extensions: %s. Maximum size: 1MB. " +
                                                                    "Only PRIMARY picture type is supported.";
    private static final String ILLEGAL_UPDATE_EXCEPTION_MESSAGE = "Can't update animal with clinical record: %s. " +
                                                                   "An animal with id: %s already has this clinical record";
    private static final String VALIDATION_FAILED = "Validation failed";

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpHeaders headers;

    private HttpStatus status;
    private RestExceptionHandler restExceptionHandler;

    @BeforeEach
    public void setUp() {
        status = HttpStatus.MULTI_STATUS;
        restExceptionHandler = new RestExceptionHandler();
    }

    @Test
    void shouldBeAnInstanceOfResponseEntityExceptionHandler() {
        assertThat(restExceptionHandler, is(instanceOf(ResponseEntityExceptionHandler.class)));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusBadRequestForHttpMessageNotReadableException() {
        HttpMessageNotReadableException httpMessageNotReadableException = mock(HttpMessageNotReadableException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.
                handleHttpMessageNotReadable(httpMessageNotReadableException, headers, status, webRequest);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForHttpMessageNotReadableException() {
        HttpMessageNotReadableException httpMessageNotReadableException = mock(HttpMessageNotReadableException.class);
        String debugMessage = randomAlphabetic(10);
        when(httpMessageNotReadableException.getMessage()).thenReturn(debugMessage);
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST, "Malformed JSON request", debugMessage
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.
                handleHttpMessageNotReadable(httpMessageNotReadableException, headers, status, webRequest);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusConflictForEntityAlreadyExistsException() {
        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException(
                mock(Throwable.class)
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.
                handleEntityAlreadyExistsException(entityAlreadyExistsException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CONFLICT));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForEntityAlreadyExistsException() {
        Throwable throwable = mock(Throwable.class);
        String localizedMessage = randomAlphabetic(10);
        when(throwable.getLocalizedMessage()).thenReturn(localizedMessage);
        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException(throwable);
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(
                HttpStatus.CONFLICT, "The resource already exists", localizedMessage
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.
                handleEntityAlreadyExistsException(entityAlreadyExistsException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusBadRequestForMethodArgumentNotValidException() {
        var bindingResult = mock(BindingResult.class);
        var methodParameter = mock(MethodParameter.class);
        var methodArgumentNotValidException = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<Object> responseEntity = restExceptionHandler.
                handleMethodArgumentNotValid(methodArgumentNotValidException, headers, status, webRequest);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForMethodArgumentNotValidException() {
        var bindingResult = mock(BindingResult.class);
        var methodParameter = mock(MethodParameter.class);
        var methodArgumentNotValidException = new MethodArgumentNotValidException(methodParameter, bindingResult);
        List<FieldError> fieldErrors = createFieldErrors();
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED)
                .setSubErrors(fieldErrors.stream()
                                         .map(fieldError -> new ValidationApiSubErrorResponse(
                                                 fieldError.getField(), fieldError.getDefaultMessage()
                                         )).collect(Collectors.toList())
                );

        ResponseEntity<Object> responseEntity = restExceptionHandler.
                handleMethodArgumentNotValid(methodArgumentNotValidException, headers, status, webRequest);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusBadRequestForConstraintViolationException() {
        ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);
        when(constraintViolationException.getConstraintViolations()).thenReturn(new HashSet<>());

        ResponseEntity<Object> responseEntity = restExceptionHandler.
                handleConstraintViolationException(constraintViolationException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForConstraintViolationException() {
        ConstraintViolationException constraintViolationException = mock(ConstraintViolationException.class);
        Set<ConstraintViolation<?>> constraintViolations = createConstraintViolations();
        when(constraintViolationException.getConstraintViolations()).thenReturn(constraintViolations);
        String debugMessage = randomAlphabetic(10);
        when(constraintViolationException.getMessage()).thenReturn(debugMessage);
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST,
                                                                         VALIDATION_FAILED,
                                                                         debugMessage)
                .setSubErrors(constraintViolations.stream()
                                                  .map(violation -> new ValidationApiSubErrorResponse(violation.getPropertyPath().toString(), violation.getMessage()))
                                                  .collect(Collectors.toList()));

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleConstraintViolationException(
                constraintViolationException
        );

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusNotFoundForEntityNotFoundException() {
        EntityNotFoundException entityNoyFoundException = mock(EntityNotFoundException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityNotFoundException(entityNoyFoundException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForEntityNotFoundException() {
        EntityNotFoundException entityNotFoundException = new EntityNotFoundException();
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(HttpStatus.NOT_FOUND, "Unable to find the resource");

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleEntityNotFoundException(entityNotFoundException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusBadRequestForIllegalUpdateException() {
        IllegalUpdateException illegalUpdateException = mock(IllegalUpdateException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleIllegalUpdateException(illegalUpdateException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForIllegalUpdateException() {
        UUID existingAnimalId = UUID.randomUUID();
        String clinicalRecord = randomAlphabetic(10);
        IllegalUpdateException illegalUpdateException = new IllegalUpdateException(existingAnimalId, clinicalRecord);
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                String.format(ILLEGAL_UPDATE_EXCEPTION_MESSAGE, clinicalRecord, existingAnimalId)
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleIllegalUpdateException(illegalUpdateException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusBadRequestForInvalidPictureException() {
        InvalidPictureException invalidPictureException = mock(InvalidPictureException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleInvalidPictureException(invalidPictureException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForInvalidPictureException() {
        InvalidPictureException invalidPictureException = new InvalidPictureException();
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                String.format(INVALID_PICTURE_EXCEPTION_MESSAGE, Arrays.stream(SupportedImageExtension.values())
                                                                       .map(SupportedImageExtension::getExtension)
                                                                       .collect(Collectors.joining(", ")))
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleInvalidPictureException(invalidPictureException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForInvalidPictureExceptionWithThrowableCause() {
        Throwable throwable = mock(Throwable.class);
        String localizedMessage = randomAlphabetic(10);
        when(throwable.getLocalizedMessage()).thenReturn(localizedMessage);
        InvalidPictureException invalidPictureException = new InvalidPictureException(throwable);
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                String.format(INVALID_PICTURE_EXCEPTION_MESSAGE, Arrays.stream(SupportedImageExtension.values())
                                                                       .map(SupportedImageExtension::getExtension)
                                                                       .collect(Collectors.joining(", "))),
                localizedMessage
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleInvalidPictureException(
                invalidPictureException
        );

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusServiceUnavailableErrorForMediaStorageException() {
        MediaStorageException mediaStorageException = mock(MediaStorageException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleMediaStorageException(mediaStorageException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.SERVICE_UNAVAILABLE));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForMediaStorageException() {
        MediaStorageException mediaStorageException = new MediaStorageException();
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Can not fulfill the request now. Please, retry later (client unavailable)"
        );

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleMediaStorageException(mediaStorageException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusForbiddenForUnauthorizedException() {
        UnauthorizedException unauthorizedException = mock(UnauthorizedException.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleUnauthorizedException(unauthorizedException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForUnauthorizedException() {
        UnauthorizedException unauthorizedException = new UnauthorizedException();
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(HttpStatus.FORBIDDEN, "Unauthorized");

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleUnauthorizedException(unauthorizedException);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    @Test
    void shouldReturnAResponseEntityWithHttpStatusServiceUnavailableForException() {
        Exception exception = mock(Exception.class);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleGlobalException(exception);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.SERVICE_UNAVAILABLE));
    }

    @Test
    void shouldReturnAResponseEntityWithApiErrorForException() {
        String message = randomAlphabetic(10);
        Exception exception = new Exception(message);
        ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, message);

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleGlobalException(exception);

        assertThat(responseEntity.getBody(), is(instanceOf(ApiErrorResponse.class)));
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) responseEntity.getBody();
        assertThat(apiErrorResponse, is(expectedApiErrorResponse));
    }

    private static List<FieldError> createFieldErrors() {
        List<FieldError> fieldErrors = new ArrayList<>();
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

    private static Set<ConstraintViolation<?>> createConstraintViolations() {
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();

        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        when(constraintViolation.getPropertyPath()).thenReturn(PathImpl.createPathFromString(randomAlphabetic(10)));
        when(constraintViolation.getMessage()).thenReturn(randomAlphabetic(10));

        ConstraintViolation<?> anotherConstraintViolation = mock(ConstraintViolation.class);
        when(anotherConstraintViolation.getPropertyPath()).thenReturn(PathImpl.createPathFromString(randomAlphabetic(10)));
        when(anotherConstraintViolation.getMessage()).thenReturn(randomAlphabetic(10));

        constraintViolations.add(constraintViolation);
        constraintViolations.add(anotherConstraintViolation);
        return constraintViolations;
    }
}