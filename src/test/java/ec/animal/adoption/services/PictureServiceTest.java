package ec.animal.adoption.services;

import ec.animal.adoption.clients.MediaStorageClient;
import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.repositories.PictureRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PictureServiceTest {

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private MediaStorageClient mediaStorageClient;

    @Test
    public void shouldCreateAPicture() {
        UUID animalUuid = UUID.randomUUID();
        String name = randomAlphabetic(10);
        PictureType pictureType = getRandomPictureType();
        ImagePicture imagePicture = new ImagePicture(
                animalUuid, name, pictureType, mock(Image.class), mock(Image.class)
        );
        LinkPicture linkPicture = new LinkPicture(
                animalUuid, name, pictureType, mock(MediaLink.class), mock(MediaLink.class)
        );
        when(mediaStorageClient.save(imagePicture)).thenReturn(linkPicture);
        when(pictureRepository.save(linkPicture)).thenReturn(linkPicture);
        PictureService pictureService = new PictureService(mediaStorageClient, pictureRepository);

        Picture createdPicture = pictureService.create(imagePicture);

        assertThat(createdPicture, is(linkPicture));
    }
}