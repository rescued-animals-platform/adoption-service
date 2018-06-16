package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MediaLinkTest {

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(MediaLink.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeJsonSerializableWithUrl() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = randomAlphabetic(10);
        String expectedSerializedMediaLink = "{\"url\":\"" + url + "\"}";
        MediaLink mediaLink = new MediaLink(url);

        String serializedMediaLink = objectMapper.writeValueAsString(mediaLink);

        assertThat(serializedMediaLink, is(expectedSerializedMediaLink));
    }
}