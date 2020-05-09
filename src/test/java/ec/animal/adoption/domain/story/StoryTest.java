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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StoryTest {

    private String text;

    @BeforeEach
    public void setUp() {
        text = randomAlphabetic(100);
    }

    @Test
    public void shouldCreateAStoryWithNoIdentifierNorRegistrationDate() {
        Story story = new Story(text);

        assertEquals(text, story.getText());
        assertNull(story.getIdentifier());
        assertNull(story.getRegistrationDate());
    }

    @Test
    public void shouldCreateAStoryWithIdentifierAndRegistrationDate() {
        UUID storyId = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();
        Story story = new Story(storyId, registrationDate, text);

        assertEquals(text, story.getText());
        assertEquals(storyId, story.getIdentifier());
        assertEquals(registrationDate, story.getRegistrationDate());
    }

    @Test
    void shouldUpdateStoryText() {
        Story existingStory = StoryBuilder.random().build();
        String newText = randomAlphabetic(50);
        Story newStory = StoryBuilder.random().withText(newText).build();

        Story updatedStory = existingStory.updateWith(newStory);

        assertAll(() -> assertEquals(existingStory.getIdentifier(), updatedStory.getIdentifier()),
                  () -> assertEquals(existingStory.getRegistrationDate(), updatedStory.getRegistrationDate()),
                  () -> assertEquals(newText, updatedStory.getText()));
    }

    @Test
    void shouldReturnSameStoryWhenUpdatedWithItself() {
        Story existingStory = StoryBuilder.random().build();

        Story updatedStory = existingStory.updateWith(existingStory);

        assertEquals(existingStory, updatedStory);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingStoryThatHasNoIdentifier() {
        Story story = StoryBuilder.random().withIdentifier(null).build();

        assertThrows(IllegalArgumentException.class, () -> {
            story.updateWith(new Story(randomAlphabetic(10)));
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingStoryThatHasNoRegistrationDate() {
        Story story = StoryBuilder.random().withRegistrationDate(null).build();

        assertThrows(IllegalArgumentException.class, () -> {
            story.updateWith(new Story(randomAlphabetic(10)));
        });
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Story.class).usingGetClass().verify();
    }
}