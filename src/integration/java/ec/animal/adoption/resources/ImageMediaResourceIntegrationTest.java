package ec.animal.adoption.resources;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.clients.gcloud.GoogleCloudStorageClient;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.media.MediaLink;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ImageMediaResourceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GoogleCloudStorageClient googleCloudStorageClient;

    private HttpHeaders headers;

    @Before
    public void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    }

    @Test
    public void shouldReturn201CreatedWithMediaLink() {
        String expectedMediaName = "PrimaryImageLarge.jpg";
        Animal animal = createAndSaveAnimal();
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test-image-large.jpg"));
        UUID animalUuid = animal.getUuid();
        
        ResponseEntity<MediaLink> response = testClient.postForEntity(
                IMAGE_URL, parameters, MediaLink.class, animalUuid, headers
        );

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        MediaLink mediaLink = response.getBody();
        assertNotNull(mediaLink);
        assertThat(mediaLink.getAnimalUuid(), is(animalUuid));
        assertThat(mediaLink.getMediaName(), is(expectedMediaName));
        assertThat(mediaLink.getUrl().contains(expectedMediaName), is(true));
        deleteMedia(mediaLink);
    }

    private void deleteMedia(MediaLink mediaLink) {
        googleCloudStorageClient.deleteMedia(mediaLink.getAnimalUuid() + "/" + mediaLink.getMediaName());
    }
}
