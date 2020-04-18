package ec.animal.adoption.domain.exception;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ImageStorageExceptionTest {

    @Test
    public void shouldReturnMessage() {
        String expectedMessage = "The image could not be stored";

        ImageStorageException imageStorageException = new ImageStorageException(mock(Throwable.class));

        assertThat(imageStorageException.getMessage(), is(expectedMessage));
    }
}