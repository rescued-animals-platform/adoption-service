package ec.animal.adoption.resources;

import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.Link;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;
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
    public void shouldCreateAnImageMedia() throws ImageMediaProcessingException, IOException,
            EntityAlreadyExistsException {
        String extension = "ext";
        String filename = randomAlphabetic(10) + "." + extension;
        long sizeInBytes = new Random().nextLong();
        byte[] content = new byte[]{};
        when(multipartFile.getOriginalFilename()).thenReturn(filename);
        when(multipartFile.getBytes()).thenReturn(content);
        when(multipartFile.getSize()).thenReturn(sizeInBytes);
        Link expectedLink = new Link(
                animalUuid, randomAlphabetic(10), randomAlphabetic(10)
        );
        when(imageMediaService.create(new ImageMedia(animalUuid, extension, content, sizeInBytes))).
                thenReturn(expectedLink);
        ImageMediaResource imageMediaResource = new ImageMediaResource(imageMediaService);

        Link createdLink = imageMediaResource.create(animalUuid, multipartFile);

        assertThat(createdLink, is(expectedLink));
    }

    @Test(expected = ImageMediaProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenInputStreamCanNotBeAccessed() throws IOException,
            ImageMediaProcessingException, EntityAlreadyExistsException {
        when(multipartFile.getOriginalFilename()).thenReturn(randomAlphabetic(10));
        when(multipartFile.getBytes()).thenThrow(IOException.class);
        ImageMediaResource imageMediaResource = new ImageMediaResource(imageMediaService);

        imageMediaResource.create(animalUuid, multipartFile);
    }

    @Test(expected = ImageMediaProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenMultipartFileIsEmpty() throws ImageMediaProcessingException,
            EntityAlreadyExistsException {
        when(multipartFile.isEmpty()).thenReturn(true);
        ImageMediaResource imageMediaResource = new ImageMediaResource(imageMediaService);

        imageMediaResource.create(animalUuid, multipartFile);
    }

    @Test(expected = ImageMediaProcessingException.class)
    public void shouldThrowImageProcessingExceptionWhenOriginalFilenameIsNull() throws ImageMediaProcessingException,
            EntityAlreadyExistsException {
        when(multipartFile.getOriginalFilename()).thenReturn(null);
        ImageMediaResource imageMediaResource = new ImageMediaResource(imageMediaService);

        imageMediaResource.create(animalUuid, multipartFile);
    }
}