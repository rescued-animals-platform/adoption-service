package ec.animal.adoption.adapter.rest.model.story;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.adapter.rest.service.StoryResponseMapper;
import ec.animal.adoption.domain.model.story.Story;
import ec.animal.adoption.domain.model.story.StoryFactory;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoryResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldBeSerializable() throws JsonProcessingException {
        String text = randomAlphabetic(50);
        Story story = new Story(text);
        StoryResponse storyResponse = StoryResponseMapper.MAPPER.toStoryResponse(story);

        String storyResponseAsJson = objectMapper.writeValueAsString(storyResponse);

        assertTrue(storyResponseAsJson.contains(String.format("\"text\":\"%s\"", text)));
    }

    @Test
    void shouldBeDeSerializable() throws JsonProcessingException, JSONException {
        Story story = StoryFactory.random().build();
        StoryResponse expectedStoryResponse = StoryResponseMapper.MAPPER.toStoryResponse(story);
        String storyResponseAsJson = new JSONObject().put("text", story.getText()).toString();

        StoryResponse storyResponse = objectMapper.readValue(storyResponseAsJson, StoryResponse.class);

        Assertions.assertThat(storyResponse).usingRecursiveComparison().isEqualTo(expectedStoryResponse);
    }
}