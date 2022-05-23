package ec.animal.adoption.api.model.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MediaLinkResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldBeSerializable() throws IOException {
        String url = randomAlphabetic(10);
        MediaLinkResponse mediaLinkResponse = MediaLinkResponse.from(url);

        String mediaLinkResponseAsJson = objectMapper.writeValueAsString(mediaLinkResponse);

        assertTrue(mediaLinkResponseAsJson.contains(String.format("\"url\":\"%s\"", url)));
    }

    @Test
    void shouldBeDeSerializable() throws IOException, JSONException {
        String url = randomAlphabetic(10);
        MediaLinkResponse expectedMediaLinkResponse = MediaLinkResponse.from(url);
        String mediaLinkResponseAsJson = new JSONObject().put("url", url).toString();

        MediaLinkResponse mediaLinkResponse = objectMapper.readValue(mediaLinkResponseAsJson, MediaLinkResponse.class);

        Assertions.assertThat(mediaLinkResponse)
                  .usingRecursiveComparison()
                  .isEqualTo(expectedMediaLinkResponse);
    }
}