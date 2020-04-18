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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.builders.OrganizationBuilder;
import ec.animal.adoption.builders.StoryBuilder;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.story.Story;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

public class JpaAnimalTest {

    @Test
    public void shouldGenerateAnUuidWhenCreatingAJpaAnimalForAnAnimalWithNoUuid() {
        Animal animal = AnimalBuilder.random().withUuid(null).build();
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertNotNull(jpaAnimalToAnimal.getUuid());
    }

    @Test
    public void shouldGenerateARegistrationDateWhenCreatingAJpaAnimalForAnAnimalWithNoRegistrationDate() {
        Animal animal = AnimalBuilder.random().withRegistrationDate(null).build();
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertNotNull(jpaAnimalToAnimal.getRegistrationDate());
    }

    @Test
    public void shouldCreateAnAnimalWithUuid() {
        UUID uuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().withUuid(uuid).build();
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertThat(jpaAnimalToAnimal.getUuid(), is(uuid));
    }

    @Test
    public void shouldCreateAnAnimalWithRegistrationDate() {
        LocalDateTime registrationDate = LocalDateTime.now();
        Animal animal = AnimalBuilder.random().withRegistrationDate(registrationDate).build();
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertThat(jpaAnimalToAnimal.getRegistrationDate(), is(registrationDate));
    }

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimal() {
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        Story story = StoryBuilder.random().build();
        Organization organization = OrganizationBuilder.random().build();
        Animal expectedAnimal = AnimalBuilder.random()
                                             .withOrganization(organization)
                                             .withPrimaryLinkPicture(primaryLinkPicture)
                                             .withCharacteristics(characteristics)
                                             .withStory(story)
                                             .build();
        JpaAnimal jpaAnimal = new JpaAnimal(expectedAnimal);

        Animal actualAnimal = jpaAnimal.toAnimal();

        assertAll(() -> assertEquals(expectedAnimal.getClinicalRecord(), actualAnimal.getClinicalRecord()),
                  () -> assertEquals(expectedAnimal.getName(), actualAnimal.getName()),
                  () -> assertEquals(expectedAnimal.getSpecies(), actualAnimal.getSpecies()),
                  () -> assertEquals(expectedAnimal.getEstimatedAge(), actualAnimal.getEstimatedAge()),
                  () -> assertEquals(expectedAnimal.getSex(), actualAnimal.getSex()),
                  () -> assertEquals(expectedAnimal.getState(), actualAnimal.getState()),
                  () -> assertEqualsPrimaryLinkPicture(expectedAnimal.getPrimaryLinkPicture(), actualAnimal.getPrimaryLinkPicture()),
                  () -> assertEqualsCharacteristics(expectedAnimal.getCharacteristics(), actualAnimal.getCharacteristics()),
                  () -> assertEquals(expectedAnimal.getStory().getText(), actualAnimal.getStory().getText()),
                  () -> assertEquals(expectedAnimal.getOrganization(), actualAnimal.getOrganization()));
    }

    private void assertEqualsPrimaryLinkPicture(final LinkPicture expectedPrimaryLinkPicture,
                                                final LinkPicture primaryLinkPicture) {
        assertThat(primaryLinkPicture.getName(), is(expectedPrimaryLinkPicture.getName()));
        assertThat(primaryLinkPicture.getPictureType(), is(expectedPrimaryLinkPicture.getPictureType()));
        assertThat(primaryLinkPicture.getLargeImageUrl(), is(expectedPrimaryLinkPicture.getLargeImageUrl()));
        assertThat(primaryLinkPicture.getSmallImageUrl(), is(expectedPrimaryLinkPicture.getSmallImageUrl()));
    }

    private void assertEqualsCharacteristics(final Characteristics expectedCharacteristics,
                                             final Characteristics characteristics) {
        assertThat(characteristics.getSize(), is(expectedCharacteristics.getSize()));
        assertThat(characteristics.getTemperaments(), is(expectedCharacteristics.getTemperaments()));
        assertThat(characteristics.getFriendlyWith(), is(expectedCharacteristics.getFriendlyWith()));
        assertThat(characteristics.getPhysicalActivity(), is(expectedCharacteristics.getPhysicalActivity()));
    }

    @Test
    public void shouldCreateAJpaAnimalWithNoJpaPrimaryLinkPicture() {
        Animal animal = AnimalBuilder.random().build();

        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertNull(jpaAnimalToAnimal.getPrimaryLinkPicture());
    }

    @Test
    public void shouldCreateAJpaAnimalWithNoJpaCharacteristics() {
        Animal animal = AnimalBuilder.random().build();

        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertNull(jpaAnimalToAnimal.getCharacteristics());
    }

    @Test
    public void shouldCreateAJpaAnimalWithNoJpaStory() {
        Animal animal = AnimalBuilder.random().build();

        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertNull(jpaAnimalToAnimal.getStory());
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaAnimal.class).usingGetClass()
                      .withPrefabValues(JpaPrimaryLinkPicture.class, mock(JpaPrimaryLinkPicture.class), mock(JpaPrimaryLinkPicture.class))
                      .withPrefabValues(JpaCharacteristics.class, mock(JpaCharacteristics.class), mock(JpaCharacteristics.class))
                      .withPrefabValues(JpaStory.class, mock(JpaStory.class), mock(JpaStory.class))
                      .suppress(Warning.SURROGATE_KEY)
                      .verify();
    }
}