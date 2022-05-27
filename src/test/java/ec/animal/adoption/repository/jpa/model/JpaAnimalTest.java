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

import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalFactory;
import ec.animal.adoption.domain.animal.dto.AnimalDto;
import ec.animal.adoption.domain.animal.dto.AnimalDtoFactory;
import ec.animal.adoption.domain.animal.characteristics.Characteristics;
import ec.animal.adoption.domain.animal.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.animal.media.LinkPicture;
import ec.animal.adoption.domain.animal.media.LinkPictureFactory;
import ec.animal.adoption.domain.animal.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import ec.animal.adoption.domain.animal.story.Story;
import ec.animal.adoption.domain.animal.story.StoryFactory;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class JpaAnimalTest {

    @Test
    void shouldBuildAJpaAnimalFromACreateAnimalDtoAndGenerateAnIdentifierAndARegistrationDateForIt() {
        Organization organization = OrganizationFactory.random().build();
        AnimalDto animalDto = AnimalDtoFactory.random().withOrganization(organization).build();

        JpaAnimal jpaAnimal = new JpaAnimal(animalDto);

        Animal actualAnimal = jpaAnimal.toAnimal();

        assertAll(() -> assertNotNull(actualAnimal.getIdentifier()),
                  () -> assertNotNull(actualAnimal.getRegistrationDate()),
                  () -> assertEquals(animalDto.getClinicalRecord(), actualAnimal.getClinicalRecord()),
                  () -> assertEquals(animalDto.getName(), actualAnimal.getName()),
                  () -> assertEquals(animalDto.getSpecies(), actualAnimal.getSpecies()),
                  () -> assertEquals(animalDto.getEstimatedAge(), actualAnimal.getEstimatedAge()),
                  () -> assertEquals(animalDto.getSex(), actualAnimal.getSex()),
                  () -> assertEquals(animalDto.getStateNameAsString(), actualAnimal.getStateName()),
                  () -> assertEquals(animalDto.getAdoptionFormId(), actualAnimal.getState().getAdoptionFormId()),
                  () -> assertEquals(animalDto.getNotes(), actualAnimal.getState().getNotes()),
                  () -> assertEquals(animalDto.getOrganization(), actualAnimal.getOrganization()));
    }

    @Test
    void shouldBuildAJpaAnimalFromAnAnimal() {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        Characteristics characteristics = CharacteristicsFactory.random().build();
        Story story = StoryFactory.random().build();
        Organization organization = OrganizationFactory.random().build();
        Animal expectedAnimal = AnimalFactory.random()
                                             .withOrganization(organization)
                                             .withPrimaryLinkPicture(primaryLinkPicture)
                                             .withCharacteristics(characteristics)
                                             .withStory(story)
                                             .build();
        JpaAnimal jpaAnimal = new JpaAnimal(expectedAnimal);

        Animal actualAnimal = jpaAnimal.toAnimal();

        assertAll(() -> assertEquals(expectedAnimal.getIdentifier(), actualAnimal.getIdentifier()),
                  () -> assertEquals(expectedAnimal.getRegistrationDate(), actualAnimal.getRegistrationDate()),
                  () -> assertEquals(expectedAnimal.getClinicalRecord(), actualAnimal.getClinicalRecord()),
                  () -> assertEquals(expectedAnimal.getName(), actualAnimal.getName()),
                  () -> assertEquals(expectedAnimal.getSpecies(), actualAnimal.getSpecies()),
                  () -> assertEquals(expectedAnimal.getEstimatedAge(), actualAnimal.getEstimatedAge()),
                  () -> assertEquals(expectedAnimal.getSex(), actualAnimal.getSex()),
                  () -> Assertions.assertThat(actualAnimal.getState()).usingRecursiveComparison()
                                  .isEqualTo(expectedAnimal.getState()),
                  () -> assertTrue(actualAnimal.getPrimaryLinkPicture().isPresent()),
                  () -> assertEqualsPrimaryLinkPicture(primaryLinkPicture, actualAnimal.getPrimaryLinkPicture().get()),
                  () -> assertTrue(actualAnimal.getCharacteristics().isPresent()),
                  () -> assertEqualsCharacteristics(characteristics, actualAnimal.getCharacteristics().get()),
                  () -> assertTrue(actualAnimal.getStory().isPresent()),
                  () -> assertEquals(story.getText(), actualAnimal.getStory().get().getText()),
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
    void shouldBuildAJpaAnimalWithNoJpaPrimaryLinkPicture() {
        Animal animal = AnimalFactory.random().build();

        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertTrue(jpaAnimalToAnimal.getPrimaryLinkPicture().isEmpty());
    }

    @Test
    void shouldBuildAJpaAnimalWithNoJpaCharacteristics() {
        Animal animal = AnimalFactory.random().build();

        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertTrue(jpaAnimalToAnimal.getCharacteristics().isEmpty());
    }

    @Test
    void shouldBuildAJpaAnimalWithNoJpaStory() {
        Animal animal = AnimalFactory.random().build();

        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertTrue(jpaAnimalToAnimal.getStory().isEmpty());
    }

    @Test
    void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaAnimal.class).usingGetClass()
                      .withPrefabValues(JpaPrimaryLinkPicture.class, mock(JpaPrimaryLinkPicture.class), mock(JpaPrimaryLinkPicture.class))
                      .withPrefabValues(JpaCharacteristics.class, mock(JpaCharacteristics.class), mock(JpaCharacteristics.class))
                      .withPrefabValues(JpaStory.class, mock(JpaStory.class), mock(JpaStory.class))
                      .suppress(Warning.SURROGATE_KEY)
                      .verify();
    }
}