package ec.animal.adoption.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StoryTest {

    private static final String STORY_TEXT_IS_REQUIRED = "Story text is required";

    private Story story;
    private String text;

    @Before
    public void setUp() {
        text = randomAlphabetic(100);
        story = new Story(text);
    }

    @Test
    public void shouldCreateAStory() {
        assertThat(story.getText(), is(text));
    }

    @Test
    public void shouldSetAnimalUuid() {
        UUID animalUuid = UUID.randomUUID();
        story.setAnimalUuid(animalUuid);

        assertThat(story.getAnimalUuid(), is(animalUuid));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Story.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String serializedStory = objectMapper.writeValueAsString(story);
        Story deserializedStory = objectMapper.readValue(serializedStory, Story.class);

        assertThat(deserializedStory, is(story));
    }

    @Test
    public void shouldValidateNonNullText() {
        Story story = new Story(null);

        Set<ConstraintViolation<Story>> constraintViolations = getValidator().validate(story);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Story> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(STORY_TEXT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("text"));
    }

    @Test
    public void shouldValidateNonEmptyText() {
        Story story = new Story("");

        Set<ConstraintViolation<Story>> constraintViolations = getValidator().validate(story);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Story> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(STORY_TEXT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("text"));
    }
}