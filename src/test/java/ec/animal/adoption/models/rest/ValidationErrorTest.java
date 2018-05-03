package ec.animal.adoption.models.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class ValidationErrorTest {

    @Test
    public void shouldBeEqualToAnotherObjectWithSameValues() {
        String field = randomAlphabetic(10);
        String message = randomAlphabetic(10);
        ValidationError validationError = new ValidationError(field, message);
        ValidationError sameValidationError = new ValidationError(field, message);

        assertEquals(validationError, sameValidationError);
    }

    @Test
    public void shouldNotBeEqualToAnotherObjectWithDifferentValues() {
        ValidationError validationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));
        ValidationError differentValidationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));

        assertNotEquals(validationError, differentValidationError);
    }

    @Test
    public void shouldBeEqualToItself() {
        ValidationError validationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));

        assertEquals(validationError, validationError);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        ValidationError validationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));

        assertNotEquals(validationError, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        ValidationError validationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));

        assertNotEquals(validationError, null);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        String field = randomAlphabetic(10);
        String message = randomAlphabetic(10);
        ValidationError validationError = new ValidationError(field, message);
        ValidationError sameValidationError = new ValidationError(field, message);

        assertEquals(validationError.hashCode(), sameValidationError.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeWhenHavingDifferentValues() {
        ValidationError validationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));
        ValidationError differentValidationError = new ValidationError(randomAlphabetic(10), randomAlphabetic(10));

        assertNotEquals(validationError.hashCode(), differentValidationError.hashCode());
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
