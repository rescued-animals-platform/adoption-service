package ec.animal.adoption.clients;

import com.google.cloud.storage.StorageException;
import ec.animal.adoption.clients.gcloud.GoogleCloudStorageClient;
import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.exceptions.ImageStorageException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MediaStorageClientGcsTest {

    @Mock
    private GoogleCloudStorageClient googleCloudStorageClient;

    private UUID animalUuid;
    private String name;
    private PictureType pictureType;
    private MediaStorageClientGcs mediaStorageClientGcs;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        name = randomAlphabetic(10);
        pictureType = getRandomPictureType();
        mediaStorageClientGcs = new MediaStorageClientGcs(googleCloudStorageClient);
    }

    @Test
    public void shouldBeAnInstanceOgMediaStorageClient() {
        assertThat(mediaStorageClientGcs, is(instanceOf(MediaStorageClient.class)));
    }

    @Test
    public void shouldStoreMedia() {
        ImageMedia imageMedia = new ImageMedia(
                UUID.randomUUID(), randomAlphabetic(3), new byte[]{}, new Random().nextLong()
        );
        String url = randomAlphabetic(10);
        when(googleCloudStorageClient.storeMedia(imageMedia.getPath(), imageMedia.getContent())).thenReturn(url);
        MediaLink expectedMediaLink = new MediaLink(imageMedia.getAnimalUuid(), imageMedia.getName(), url);

        MediaLink mediaLink = mediaStorageClientGcs.save(imageMedia);

        assertThat(mediaLink, is(expectedMediaLink));
    }

    @Test(expected = ImageProcessingException.class)
    public void shouldThrowImageMediaProcessingException() {
        ImageMedia imageMedia = new ImageMedia(
                UUID.randomUUID(), randomAlphabetic(3), new byte[]{}, new Random().nextLong()
        );
        when(googleCloudStorageClient.storeMedia(imageMedia.getPath(), imageMedia.getContent())).
                thenThrow(StorageException.class);

        mediaStorageClientGcs.save(imageMedia);
    }

    @Test
    public void shouldStorePicture() {
        ImagePicture imagePicture = new ImagePicture(
                animalUuid, name, pictureType, mock(Image.class), mock(Image.class)
        );
        String largeImageUrl = randomAlphabetic(10);
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getLargeImagePath(), imagePicture.getLargeImageContent()
        )).thenReturn(largeImageUrl);
        String smallImageUrl = randomAlphabetic(10);
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getSmallImagePath(), imagePicture.getSmallImageContent()
        )).thenReturn(smallImageUrl);
        LinkPicture expectedPicture = new LinkPicture(
                animalUuid, name, pictureType, new MediaLink(largeImageUrl), new MediaLink(smallImageUrl)
        );

        Picture storedPicture = mediaStorageClientGcs.save(imagePicture);

        assertThat(storedPicture, is(expectedPicture));
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringLargeImage() {
        ImagePicture imagePicture = new ImagePicture(
                animalUuid, name, pictureType, mock(Image.class), mock(Image.class)
        );
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getLargeImagePath(), imagePicture.getLargeImageContent()
        )).thenThrow(StorageException.class);

        mediaStorageClientGcs.save(imagePicture);
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringSmallImage() {
        ImagePicture imagePicture = new ImagePicture(
                animalUuid, name, pictureType, mock(Image.class), mock(Image.class)
        );
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getSmallImagePath(), imagePicture.getSmallImageContent()
        )).thenThrow(StorageException.class);

        mediaStorageClientGcs.save(imagePicture);
    }
}