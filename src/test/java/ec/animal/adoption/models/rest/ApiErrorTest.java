package ec.animal.adoption.models.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ec.animal.adoption.models.rest.suberrors.ApiSubError;
import ec.animal.adoption.models.rest.suberrors.ValidationError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApiErrorTest {

    private String debugMessage;
    private HttpStatus status;
    private String message;
    private ApiError apiError;
    private List<ApiSubError> subErrors;

    @Before
    public void setUp() {
        debugMessage = randomAlphabetic(10);
        status = HttpStatus.BAD_GATEWAY;
        message = randomAlphabetic(10);
        subErrors = new ArrayList<>();
        subErrors.add(new ValidationError(randomAlphabetic(10), randomAlphabetic(10)));
        subErrors.add(new ValidationError(randomAlphabetic(10), randomAlphabetic(10)));
    }

    @Test
    public void shouldCreateApiErrorFromStatusMessageAndDebugMessage() {
        apiError = new ApiError(status, message, debugMessage);

        assertThat(apiError.getStatus(), is(status));
        assertThat(apiError.getMessage(), is(message));
        assertThat(apiError.getDebugMessage(), is(debugMessage));
        assertThat(apiError.getTimestamp(), is(instanceOf(LocalDateTime.class)));
    }

    @Test
    public void shouldCreateApiErrorFromStatusAndMessage() {
        apiError = new ApiError(status, message);

        assertThat(apiError.getStatus(), is(status));
        assertThat(apiError.getMessage(), is(message));
        assertThat(apiError.getDebugMessage(), is(""));
        assertThat(apiError.getTimestamp(), is(instanceOf(LocalDateTime.class)));
    }

    @Test
    public void shouldSetApiSubErrors() {
        apiError = new ApiError(status, message, debugMessage);
        apiError.setSubErrors(subErrors);

        assertThat(apiError.getSubErrors(), is(subErrors));
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        apiError = new ApiError(status, message, debugMessage);
        apiError.setSubErrors(subErrors);
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String serializedApiError = objectMapper.writeValueAsString(apiError);

        ApiError deserializedApiError = objectMapper.readValue(
                serializedApiError, ApiError.class
        );

        assertThat(deserializedApiError.getTimestamp(), is(apiError.getTimestamp()));
        assertThat(deserializedApiError.getStatus(), is(apiError.getStatus()));
        assertThat(deserializedApiError.getMessage(), is(apiError.getMessage()));
        assertThat(deserializedApiError.getDebugMessage(), is(apiError.getDebugMessage()));
        assertThat(deserializedApiError.getSubErrors(), is(subErrors));
    }
}