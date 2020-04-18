package ec.animal.adoption.domain.exception;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class InvalidPictureExceptionTest {

    @Test
    public void shouldReturnMessage() {
        InvalidPictureException invalidPictureException = new InvalidPictureException(mock(Throwable.class));

        assertThat(invalidPictureException.getMessage(), is(notNullValue()));
    }
}