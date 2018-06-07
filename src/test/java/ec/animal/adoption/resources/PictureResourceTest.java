package ec.animal.adoption.resources;

import ec.animal.adoption.domain.picture.Image;
import ec.animal.adoption.domain.picture.MediaLink;
import ec.animal.adoption.domain.picture.Picture;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.services.PictureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

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
        String filename = randomAlphabetic(10);
        long sizeInBytes = new Random().nextLong();
        byte[] bytes = new byte[]{};
        when(multipartFile.getName()).thenReturn(name);
        when(multipartFile.getOriginalFilename()).thenReturn(filename);
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getBytes()).thenReturn(bytes);
        when(multipartFile.getSize()).thenReturn(sizeInBytes);
        Picture expectedPicture = new Picture(name, filename, sizeInBytes, new MediaLink(randomAlphabetic(10)));
        when(pictureService.create(
                new Picture(name, filename, sizeInBytes, new Image(inputStream, bytes)))
        ).thenReturn(expectedPicture);
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

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenMultipartFileIsEmpty() throws ImageProcessingException {
        when(multipartFile.isEmpty()).thenReturn(true);
        PictureResource pictureResource = new PictureResource(pictureService);

        pictureResource.create(multipartFile);
    }
}