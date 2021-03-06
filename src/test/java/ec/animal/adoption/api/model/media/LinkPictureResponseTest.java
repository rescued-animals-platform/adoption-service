package ec.animal.adoption.api.model.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.LinkPictureFactory;
import ec.animal.adoption.domain.media.PictureType;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LinkPictureResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    public void shouldBeSerializable() throws IOException {
        LinkPicture linkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        LinkPictureResponse linkPictureResponse = LinkPictureResponse.from(linkPicture);

        String linkPictureResponseAsJson = objectMapper.writeValueAsString(linkPictureResponse);

        assertTrue(linkPictureResponseAsJson.contains(String.format("\"name\":\"%s\"", linkPicture.getName())));
        assertTrue(linkPictureResponseAsJson.contains(
                String.format("\"pictureType\":\"%s\"", linkPicture.getPictureType().name()))
        );
        assertTrue(linkPictureResponseAsJson.contains(
                String.format("\"largeImageMediaLink\":{\"url\":\"%s\"}", linkPicture.getLargeImageUrl()))
        );
        assertTrue(linkPictureResponseAsJson.contains(
                String.format("\"smallImageMediaLink\":{\"url\":\"%s\"}", linkPicture.getSmallImageUrl()))
        );
    }

    @Test
    public void shouldBeDeSerializable() throws IOException, JSONException {
        LinkPicture linkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        LinkPictureResponse expectedLinkPictureResponse = LinkPictureResponse.from(linkPicture);
        String linkPictureResponseAsJson = new JSONObject()
                .put("name", linkPicture.getName())
                .put("pictureType", linkPicture.getPictureType().name())
                .put("largeImageMediaLink", new JSONObject().put("url", linkPicture.getLargeImageUrl()))
                .put("smallImageMediaLink", new JSONObject().put("url", linkPicture.getSmallImageUrl()))
                .toString();

        LinkPictureResponse linkPictureResponse = objectMapper.readValue(linkPictureResponseAsJson,
                                                                         LinkPictureResponse.class);

        Assertions.assertThat(linkPictureResponse)
                  .usingRecursiveComparison()
                  .isEqualTo(expectedLinkPictureResponse);
    }
}