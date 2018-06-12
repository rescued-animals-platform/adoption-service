package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LinkTest {

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Link.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeJsonDeserializableWithAnimalUuidMediaNameAndUrl() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UUID animalUuid = UUID.randomUUID();
        String mediaName = randomAlphabetic(10);
        String url = randomAlphabetic(10);
        String serializedLink = "{\"animalUuid\":\"" + animalUuid + "\",\"mediaName\":\"" + mediaName +
                "\",\"url\":\"" + url + "\"}";
        Link expectedLink = new Link(animalUuid, mediaName, url);

        Link link = objectMapper.readValue(serializedLink, Link.class);

        assertThat(link, is(expectedLink));
    }
}