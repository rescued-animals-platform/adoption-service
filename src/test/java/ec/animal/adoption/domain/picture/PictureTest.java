package ec.animal.adoption.domain.picture;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PictureTest {

    private String name;
    private String filename;

    @Before
    public void setUp() {
        name = randomAlphabetic(10);
        filename = randomAlphabetic(10);
    }

    @Test
    public void shouldCreateAPictureWithANameFilenameAndMediaLink() {
        String url = randomAlphabetic(10);
        PictureRepresentation mediaLink = new MediaLink(url);
        Picture picture = new Picture(name, filename, mediaLink);

        assertThat(picture.getName(), is(name));
        assertThat(picture.getFilename(), is(filename));
    }

    @Test
    public void shouldReturnEmptyInputStreamWhenAPictureWasCreatedWithAMediaLinkPictureRepresentation() {
        Picture picture = new Picture(name, filename, new MediaLink(randomAlphabetic(10)));

        assertFalse(picture.getInputStream().isPresent());
    }

    @Test
    public void shouldCreateAPictureWithAFilenameAndAnImage() {
        InputStream inputStream = mock(InputStream.class);
        PictureRepresentation image = new Image(inputStream);
        Picture picture = new Picture(name, filename, image);

        assertThat(picture.getName(), is(name));
        assertTrue(picture.getInputStream().isPresent());
        assertThat(picture.getInputStream().get(), is(inputStream));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Picture.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeJsonDeserializableWithNameFilenameAndMediaLink() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = randomAlphabetic(10);
        String serializedPicture = "{\"name\":\""+ name + "\",\"filename\":\"" + filename +
                "\",\"mediaLink\":{\"url\":\"" + url + "\"}}";
        Picture expectedPicture = new Picture(name, filename, new MediaLink(url));

        Picture picture = objectMapper.readValue(serializedPicture, Picture.class);

        assertThat(picture, is(expectedPicture));
    }
}