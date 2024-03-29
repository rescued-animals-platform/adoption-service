package ec.animal.adoption.adapter.rest.model.story;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.adapter.rest.service.StoryRequestMapper;
import ec.animal.adoption.domain.model.story.Story;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Set;

import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoryRequestTest {

    private static final String STORY_TEXT_IS_REQUIRED = "Story text is required";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldBeSerializable() throws JsonProcessingException {
        String text = randomAlphabetic(50);
        StoryRequest storyRequest = new StoryRequest(text);

        String storyRequestAsJson = objectMapper.writeValueAsString(storyRequest);

        assertTrue(storyRequestAsJson.contains(String.format("\"text\":\"%s\"", text)));
    }

    @Test
    void shouldDeSerializeAndReturnStory() throws IOException, JSONException {
        String expectedText = randomAlphabetic(100);
        String storyRequestAsJson = new JSONObject().put("text", expectedText).toString();

        StoryRequest storyRequest = objectMapper.readValue(storyRequestAsJson, StoryRequest.class);
        Story story = StoryRequestMapper.MAPPER.toStory(storyRequest);

        assertEquals(expectedText, story.getText());
        assertNull(story.getIdentifier());
        assertNull(story.getRegistrationDate());
    }

    @Test
    void shouldValidateNonNullText() {
        StoryRequest storyRequest = new StoryRequest(null);

        Set<ConstraintViolation<StoryRequest>> constraintViolations = getValidator().validate(storyRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<StoryRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(STORY_TEXT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("text"));
    }

    @Test
    void shouldValidateNonEmptyText() {
        StoryRequest storyRequest = new StoryRequest("");

        Set<ConstraintViolation<StoryRequest>> constraintViolations = getValidator().validate(storyRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<StoryRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(STORY_TEXT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("text"));
    }
}