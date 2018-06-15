package ec.animal.adoption.services;

import ec.animal.adoption.clients.ImageMediaStorageClient;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.repositories.MediaLinkRepository;
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
    private MediaLinkRepository mediaLinkRepository;

    @Mock
    private ImageMediaStorageClient imageMediaStorageClient;

    @Test
    public void shouldCreateAnImageMedia() throws ImageProcessingException, EntityAlreadyExistsException {
        UUID animalUuid = UUID.randomUUID();
        ImageMedia imageMedia = new ImageMedia(
                animalUuid, randomAlphabetic(3), new byte[]{}, new Random().nextLong()
        );
        MediaLink mediaLink = new MediaLink(animalUuid, randomAlphabetic(10), randomAlphabetic(10));
        when(imageMediaStorageClient.save(imageMedia)).thenReturn(mediaLink);
        when(mediaLinkRepository.save(mediaLink)).thenReturn(mediaLink);
        ImageMediaService imageMediaService = new ImageMediaService(imageMediaStorageClient, mediaLinkRepository);

        MediaLink createdMediaLink = imageMediaService.create(imageMedia);

        assertThat(createdMediaLink, is(mediaLink));
    }
}