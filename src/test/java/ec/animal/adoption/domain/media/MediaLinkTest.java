package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MediaLinkTest {

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(MediaLink.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeJsonDeserializableWithAnimalUuidMediaNameAndUrl() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UUID animalUuid = UUID.randomUUID();
        String mediaName = randomAlphabetic(10);
        String url = randomAlphabetic(10);
        String serializedMediaLink = "{\"animalUuid\":\"" + animalUuid + "\",\"mediaName\":\"" + mediaName +
                "\",\"url\":\"" + url + "\"}";
        MediaLink expectedMediaLink = new MediaLink(animalUuid, mediaName, url);

        MediaLink mediaLink = objectMapper.readValue(serializedMediaLink, MediaLink.class);

        assertThat(mediaLink, is(expectedMediaLink));
    }
}