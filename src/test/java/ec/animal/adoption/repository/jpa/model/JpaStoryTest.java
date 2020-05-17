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

package ec.animal.adoption.repository.jpa.model;

import ec.animal.adoption.domain.story.StoryFactory;
import ec.animal.adoption.domain.story.Story;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class JpaStoryTest {

    @Test
    public void shouldGenerateAnIdWhenCreatingAJpaStoryForAStoryWithNoId() {
        Story story = StoryFactory.random().withIdentifier(null).build();
        JpaStory jpaStory = new JpaStory(story, mock(JpaAnimal.class));

        Story jpaStoryToStory = jpaStory.toStory();

        assertNotNull(jpaStoryToStory.getIdentifier());
    }

    @Test
    public void shouldGenerateARegistrationDateWhenCreatingAJpaStoryForAStoryWithNoRegistrationDate() {
        Story story = StoryFactory.random().withRegistrationDate(null).build();
        JpaStory jpaStory = new JpaStory(story, mock(JpaAnimal.class));

        Story jpaStoryToStory = jpaStory.toStory();

        assertNotNull(jpaStoryToStory.getRegistrationDate());
    }

    @Test
    public void shouldCreateAStoryWithId() {
        UUID storyId = UUID.randomUUID();
        Story story = StoryFactory.random().withIdentifier(storyId).build();
        JpaStory jpaStory = new JpaStory(story, mock(JpaAnimal.class));

        Story jpaStoryToStory = jpaStory.toStory();

        assertThat(jpaStoryToStory.getIdentifier(), is(storyId));
    }

    @Test
    public void shouldCreateAStoryWithRegistrationDate() {
        LocalDateTime registrationDate = LocalDateTime.now();
        Story story = StoryFactory.random().withRegistrationDate(registrationDate).build();
        JpaStory jpaStory = new JpaStory(story, mock(JpaAnimal.class));

        Story jpaStoryToStory = jpaStory.toStory();

        assertThat(jpaStoryToStory.getRegistrationDate(), is(registrationDate));
    }

    @Test
    public void shouldCreateJpaStoryFromStory() {
        String text = randomAlphabetic(100);
        Story story = StoryFactory.random().withText(text).build();
        JpaStory jpaStory = new JpaStory(story, mock(JpaAnimal.class));

        Story jpaStoryToStory = jpaStory.toStory();

        assertThat(jpaStoryToStory.getText(), is(story.getText()));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaStory.class).usingGetClass()
                      .withPrefabValues(JpaAnimal.class, mock(JpaAnimal.class), mock(JpaAnimal.class))
                      .suppress(Warning.NONFINAL_FIELDS, Warning.REFERENCE_EQUALITY, Warning.SURROGATE_KEY).verify();
    }
}