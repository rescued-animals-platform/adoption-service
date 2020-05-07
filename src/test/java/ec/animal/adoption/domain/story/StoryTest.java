/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.domain.story;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StoryTest {

    private static final String STORY_TEXT_IS_REQUIRED = "Story text is required";

    private Story story;
    private String text;

    @BeforeEach
    public void setUp() {
        text = randomAlphabetic(100);
        story = StoryBuilder.random().withText(text).build();
    }

    @Test
    public void shouldCreateAStory() {
        assertThat(story.getText(), is(text));
    }

    @Test
    void shouldUpdateStoryTextAndRegistrationTime() {
        String newText = randomAlphabetic(50);
        Story newStory = StoryBuilder.random().withText(newText).build();

        Story updatedStory = story.updateWith(newStory);

        assertAll(() -> assertEquals(story.getIdentifier(), updatedStory.getIdentifier()),
                  () -> assertNotEquals(story.getRegistrationDate(), updatedStory.getRegistrationDate()),
                  () -> assertEquals(LocalDateTime.now().toLocalDate(), updatedStory.getRegistrationDate().toLocalDate()),
                  () -> assertEquals(newText, updatedStory.getText()));
    }

    @Test
    void shouldReturnSameStoryWhenUpdatedWithItself() {
        Story updatedStory = story.updateWith(story);

        assertEquals(story, updatedStory);
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Story.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();

        String serializedStory = objectMapper.writeValueAsString(story);
        Story deserializedStory = objectMapper.readValue(serializedStory, Story.class);

        assertThat(deserializedStory, is(story));
    }

    @Test
    public void shouldNotIncludeIdAndRegistrationDateInSerialization() throws JsonProcessingException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        Story story = StoryBuilder.random().withIdentifier(UUID.randomUUID()).withRegistrationDate(LocalDateTime.now())
                                  .build();

        String serializedStory = objectMapper.writeValueAsString(story);

        assertThat(serializedStory, not(containsString("\"id\":")));
        assertThat(serializedStory, not(containsString("\"registrationDate\":")));
    }

    @Test
    public void shouldNotIncludeIdAndRegistrationDateInDeserialization() throws IOException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        String idAsJson = String.format("\"id\":\"%s\"", UUID.randomUUID());
        String registrationDateAsJson = String.format("\"registrationDate\":\"%s\"", LocalDateTime.now());
        String serializedStory = String.format("{%s,%s,\"text\":\"anyTest\"}", idAsJson, registrationDateAsJson);

        Story story = objectMapper.readValue(serializedStory, Story.class);

        assertNull(story.getIdentifier());
        assertNull(story.getRegistrationDate());
    }

    @Test
    public void shouldValidateNonNullText() {
        Story story = StoryBuilder.random().withText(null).build();

        Set<ConstraintViolation<Story>> constraintViolations = getValidator().validate(story);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Story> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(STORY_TEXT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("text"));
    }

    @Test
    public void shouldValidateNonEmptyText() {
        Story story = StoryBuilder.random().withText("").build();

        Set<ConstraintViolation<Story>> constraintViolations = getValidator().validate(story);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Story> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(STORY_TEXT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("text"));
    }
}