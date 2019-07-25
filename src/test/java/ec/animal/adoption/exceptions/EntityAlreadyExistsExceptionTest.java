package ec.animal.adoption.exceptions;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class EntityAlreadyExistsExceptionTest {

    @Test
    public void shouldReturnMessage() {
        String expectedMessage = "The resource already exists";

        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException(mock(Throwable.class));

        assertThat(entityAlreadyExistsException.getMessage(), is(expectedMessage));
    }
}