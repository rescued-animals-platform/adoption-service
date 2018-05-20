package ec.animal.adoption.models.rest.suberrors;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidationErrorTest {

    @Test
    public void shouldBeAnInstanceOfApiSubError() {
        ValidationError validationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));

        assertThat(validationError, is(instanceOf(ApiSubError.class)));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(ValidationError.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ValidationError validationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));

        String serializedValidationError = objectMapper.writeValueAsString(validationError);
        ValidationError deserializedValidationError = objectMapper.readValue(serializedValidationError, ValidationError.class);

        assertThat(deserializedValidationError, is(validationError));
    }
}
