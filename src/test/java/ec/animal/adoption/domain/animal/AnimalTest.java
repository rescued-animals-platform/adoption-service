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

package ec.animal.adoption.domain.animal;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.CharacteristicsBuilder;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.LinkPictureBuilder;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationBuilder;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.story.Story;
import ec.animal.adoption.domain.story.StoryBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static ec.animal.adoption.TestUtils.getRandomState;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnimalTest {

    @Test
    public void shouldReturnStateName() {
        State randomState = getRandomState();
        Animal animal = AnimalBuilder.random().withState(randomState).build();

        assertThat(animal.getStateName(), is(randomState.getName().name()));
    }

    @Test
    public void shouldSetPrimaryLinkPicture() {
        Animal animal = AnimalBuilder.random().build();
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        Animal animalWithPrimaryLinkPicture = Animal.AnimalBuilder.copyOf(animal).with(primaryLinkPicture).build();

        assertTrue(animalWithPrimaryLinkPicture.getPrimaryLinkPicture().isPresent());
        assertEquals(primaryLinkPicture, animalWithPrimaryLinkPicture.getPrimaryLinkPicture().get());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenSettingAnAlternateLinkPicture() {
        Animal animal = AnimalBuilder.random().build();
        LinkPicture alternateLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.ALTERNATE).build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Animal.AnimalBuilder.copyOf(animal).with(alternateLinkPicture).build();
        });
        assertEquals("Picture type should be PRIMARY", exception.getMessage());
    }

    @Test
    public void shouldSetCharacteristics() {
        Animal animal = AnimalBuilder.random().build();
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        Animal animalWithCharacteristics = Animal.AnimalBuilder.copyOf(animal).with(characteristics).build();

        assertTrue(animalWithCharacteristics.getCharacteristics().isPresent());
        assertEquals(characteristics, animalWithCharacteristics.getCharacteristics().get());
    }

    @Test
    public void shouldSetStory() {
        Animal animal = AnimalBuilder.random().build();
        Story story = StoryBuilder.random().build();
        Animal animalWithStory = Animal.AnimalBuilder.copyOf(animal).with(story).build();

        assertTrue(animalWithStory.getStory().isPresent());
        assertEquals(story, animalWithStory.getStory().get());
    }

    @Test
    public void shouldSetOrganization() {
        Animal animal = AnimalBuilder.random().withOrganization(null).build();
        Organization organization = OrganizationBuilder.random().build();
        Animal animalWithOrganization = Animal.AnimalBuilder.copyOf(animal).with(organization).build();

        assertThat(animalWithOrganization.getOrganization(), is(organization));
    }

    @Test
    void shouldReturnTrueWhenAnimalAlreadyHasTheStoryProvided() {
        Story story = StoryBuilder.random().build();
        Animal animal = AnimalBuilder.random().withStory(story).build();

        assertTrue(animal.has(story));
    }

    @Test
    void shouldReturnFalseWhenAnimalDoesNotHaveAnyStory() {
        Story story = StoryBuilder.random().build();
        Animal animal = AnimalBuilder.random().withStory(null).build();

        assertFalse(animal.has(story));
    }

    @Test
    void shouldReturnFalseWhenAnimalHasADifferentStory() {
        Story story = StoryBuilder.random().build();
        Animal animal = AnimalBuilder.random().withStory(StoryBuilder.random().build()).build();

        assertFalse(animal.has(story));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Animal.class)
                      .suppress(Warning.NONFINAL_FIELDS)
                      .usingGetClass()
                      .withNonnullFields("clinicalRecord", "name", "species", "estimatedAge", "sex", "state")
                      .verify();
    }
}