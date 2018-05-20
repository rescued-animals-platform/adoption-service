package ec.animal.adoption.models.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ec.animal.adoption.models.rest.suberrors.ApiSubError;
import ec.animal.adoption.models.rest.suberrors.ValidationError;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApiErrorTest {

    private ApiError apiError;

    @Before
    public void setUp() {
        String debugMessage = randomAlphabetic(10);
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        String message = randomAlphabetic(10);
        List<ApiSubError> subErrors = new ArrayList<>();
        subErrors.add(new ValidationError(randomAlphabetic(10), randomAlphabetic(10)));
        subErrors.add(new ValidationError(randomAlphabetic(10), randomAlphabetic(10)));
        apiError = new ApiError(status, message, debugMessage);
        apiError.setSubErrors(subErrors);
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(ApiError.class)
                .usingGetClass()
                .withIgnoredFields("timestamp")
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String serializedApiError = objectMapper.writeValueAsString(apiError);

        ApiError deserializedApiError = objectMapper.readValue(
                serializedApiError, ApiError.class
        );

        assertThat(deserializedApiError, is(apiError));
    }
}