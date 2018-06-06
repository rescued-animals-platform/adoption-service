package ec.animal.adoption.domain.picture;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PictureTest {

    @Mock
    private PictureRepresentation pictureRepresentation;

    private String name;

    @Before
    public void setUp() {
        name = randomAlphabetic(10);
    }

    @Test
    public void shouldCreateAPictureWithAFilenameAndAPictureRepresentation() {
        Picture picture = new Picture(name, pictureRepresentation);

        assertThat(picture.getName(), is(name));
        assertThat(picture.getPictureRepresentation(), is(pictureRepresentation));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Picture.class).usingGetClass().verify();
    }
}