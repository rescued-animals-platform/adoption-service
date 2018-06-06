package ec.animal.adoption.resources;

import ec.animal.adoption.domain.picture.Image;
import ec.animal.adoption.domain.picture.Picture;
import ec.animal.adoption.domain.picture.Url;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.services.PictureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PictureResourceTest {

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private InputStream inputStream;

    @Mock
    private PictureService pictureService;

    @Test
    public void shouldCreateAPicture() throws ImageProcessingException, IOException {
        String name = randomAlphabetic(10);
        when(multipartFile.getName()).thenReturn(name);
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        Picture expectedPicture = new Picture(name, new Url(URI.create(randomAlphabetic(10))));
        Picture picture = new Picture(name, new Image(inputStream));
        when(pictureService.create(picture)).thenReturn(expectedPicture);
        PictureResource pictureResource = new PictureResource(pictureService);

        Picture createdPicture = pictureResource.create(multipartFile);

        assertThat(createdPicture, is(expectedPicture));
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenInputStreamCanNotBeAccessed() throws IOException,
            ImageProcessingException {
        when(multipartFile.getInputStream()).thenThrow(IOException.class);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(multipartFile);
    }
}