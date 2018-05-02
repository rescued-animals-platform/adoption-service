package ec.animal.adoption.models.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiErrorTest {

    private ApiError apiError;
    private String debugMessage;
    private HttpStatus status;
    private String message;
    private Throwable exception;

    @Before
    public void setUp() {
        exception = mock(Throwable.class);
        debugMessage = randomAlphabetic(10);
        when(exception.getLocalizedMessage()).thenReturn(debugMessage);
        status = HttpStatus.BAD_GATEWAY;
        message = randomAlphabetic(10);

    }

    @Test
    public void shouldCreateApiErrorFromStatusMessageAndException() {
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
    public void shouldBeSerializableAndDeserializable() throws IOException {
        apiError = new ApiError(status, message, debugMessage);
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
    }
}