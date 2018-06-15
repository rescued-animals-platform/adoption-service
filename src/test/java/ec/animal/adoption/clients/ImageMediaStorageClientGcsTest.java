package ec.animal.adoption.clients;

import com.google.cloud.storage.StorageException;
import ec.animal.adoption.clients.gcloud.GoogleCloudStorageClient;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.ImageProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageMediaStorageClientGcsTest {

    @Mock
    private GoogleCloudStorageClient googleCloudStorageClient;

    private ImageMediaStorageClientGcs mediaStorageClientGcs;

    @Before
    public void setUp() {
        mediaStorageClientGcs = new ImageMediaStorageClientGcs(googleCloudStorageClient);
    }

    @Test
    public void shouldBeAnInstanceOgMediaStorageClient() {
        assertThat(mediaStorageClientGcs, is(instanceOf(ImageMediaStorageClient.class)));
    }

    @Test
    public void shouldStoreMedia() throws ImageProcessingException {
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
    public void shouldThrowImageMediaProcessingException() throws ImageProcessingException {
        ImageMedia imageMedia = new ImageMedia(
                UUID.randomUUID(), randomAlphabetic(3), new byte[]{}, new Random().nextLong()
        );
        when(googleCloudStorageClient.storeMedia(imageMedia.getPath(), imageMedia.getContent())).
                thenThrow(StorageException.class);

        mediaStorageClientGcs.save(imageMedia);
    }
}