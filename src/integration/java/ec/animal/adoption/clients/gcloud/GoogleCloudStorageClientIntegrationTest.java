package ec.animal.adoption.clients.gcloud;

import ec.animal.adoption.AnimalAdoptionApplication;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = AnimalAdoptionApplication.class)
public class GoogleCloudStorageClientIntegrationTest {

    @Autowired
    private GoogleCloudStorageClient googleCloudStorageClient;

    private String mediaPath;

    @Before
    public void setUp() {
        mediaPath = UUID.randomUUID() + "/PrimaryImageLarge.jpg";
    }

    @Test
    public void shouldSaveALargeFile() throws IOException {
        InputStream inputStream = new ClassPathResource("test-image-large.jpg").getInputStream();
        byte[] fileContent = IOUtils.toByteArray(inputStream);

        String mediaLink = googleCloudStorageClient.storeMedia(mediaPath, fileContent);

        assertNotNull(mediaLink);
    }

    @After
    public void tearDown() {
        boolean mediaDeleted = googleCloudStorageClient.deleteMedia(mediaPath);
        assertTrue(mediaDeleted);
    }
}
