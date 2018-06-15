package ec.animal.adoption.resources;

import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.services.ImageMediaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageMediaResourceTest {

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private ImageMediaService imageMediaService;

    private UUID animalUuid;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
    }

    @Test
    public void shouldCreateAnImageMedia() throws ImageProcessingException, IOException,
            EntityAlreadyExistsException {
        String extension = "ext";
        String filename = randomAlphabetic(10) + "." + extension;
        long sizeInBytes = new Random().nextLong();
        byte[] content = new byte[]{};
        when(multipartFile.getOriginalFilename()).thenReturn(filename);
        when(multipartFile.getBytes()).thenReturn(content);
        when(multipartFile.getSize()).thenReturn(sizeInBytes);
        MediaLink expectedMediaLink = new MediaLink(
                animalUuid, randomAlphabetic(10), randomAlphabetic(10)
        );
        when(imageMediaService.create(new ImageMedia(animalUuid, extension, content, sizeInBytes))).
                thenReturn(expectedMediaLink);
        ImageMediaResource imageMediaResource = new ImageMediaResource(imageMediaService);

        MediaLink createdMediaLink = imageMediaResource.create(animalUuid, multipartFile);

        assertThat(createdMediaLink, is(expectedMediaLink));
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenInputStreamCanNotBeAccessed() throws IOException,
            ImageProcessingException, EntityAlreadyExistsException {
        when(multipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(multipartFile.getBytes()).thenThrow(IOException.class);
        ImageMediaResource imageMediaResource = new ImageMediaResource(imageMediaService);

        imageMediaResource.create(animalUuid, multipartFile);
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenMultipartFileIsEmpty() throws ImageProcessingException,
            EntityAlreadyExistsException {
        when(multipartFile.isEmpty()).thenReturn(true);
        ImageMediaResource imageMediaResource = new ImageMediaResource(imageMediaService);

        imageMediaResource.create(animalUuid, multipartFile);
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenOriginalFilenameIsNull() throws ImageProcessingException,
            EntityAlreadyExistsException {
        when(multipartFile.getOriginalFilename()).thenReturn(null);
        ImageMediaResource imageMediaResource = new ImageMediaResource(imageMediaService);

        imageMediaResource.create(animalUuid, multipartFile);
    }
}