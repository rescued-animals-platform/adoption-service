package ec.animal.adoption.domain.picture;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ImageTest {

    @Test
    public void shouldBeAnInstanceOfPictureRepresentation() {
        Image image = new Image(mock(InputStream.class));

        assertThat(image, is(instanceOf(PictureRepresentation.class)));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Image.class).usingGetClass().verify();
    }
}