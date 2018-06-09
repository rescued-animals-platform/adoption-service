package ec.animal.adoption.services;

import ec.animal.adoption.clients.MediaStorageClient;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;
import ec.animal.adoption.repositories.PictureRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageMediaServiceTest {

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private MediaStorageClient mediaStorageClient;

    @Test
    public void shouldCreateAnImageMedia() throws ImageMediaProcessingException {
        UUID animalUuid = UUID.randomUUID();
        ImageMedia imageMedia = new ImageMedia(
                animalUuid, randomAlphabetic(3), new byte[]{}, new Random().nextLong()
        );
        MediaLink mediaLink = new MediaLink(animalUuid, randomAlphabetic(10), randomAlphabetic(10));
        when(mediaStorageClient.save(imageMedia)).thenReturn(mediaLink);
        when(pictureRepository.save(mediaLink)).thenReturn(mediaLink);
        ImageMediaService imageMediaService = new ImageMediaService(mediaStorageClient, pictureRepository);

        MediaLink createdMediaLink = imageMediaService.create(imageMedia);

        assertThat(createdMediaLink, is(mediaLink));
    }
}