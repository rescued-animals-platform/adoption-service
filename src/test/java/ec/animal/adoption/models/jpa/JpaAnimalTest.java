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

package ec.animal.adoption.models.jpa;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.builders.StoryBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Story;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("PMD.TooManyMethods")
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
        Animal animal = AnimalBuilder.random().withPrimaryLinkPicture(primaryLinkPicture)
                                     .withCharacteristics(characteristics).withStory(story).build();
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertThat(jpaAnimalToAnimal.getClinicalRecord(), is(animal.getClinicalRecord()));
        assertThat(jpaAnimalToAnimal.getName(), is(animal.getName()));
        assertThat(jpaAnimalToAnimal.getSpecies(), is(animal.getSpecies()));
        assertThat(jpaAnimalToAnimal.getEstimatedAge(), is(animal.getEstimatedAge()));
        assertThat(jpaAnimalToAnimal.getSex(), is(animal.getSex()));
        assertThat(jpaAnimalToAnimal.getState(), is(animal.getState()));
        assertEqualsPrimaryLinkPicture(jpaAnimalToAnimal.getPrimaryLinkPicture(), animal.getPrimaryLinkPicture());
        assertEqualsCharacteristics(jpaAnimalToAnimal.getCharacteristics(), animal.getCharacteristics());
        assertThat(jpaAnimalToAnimal.getStory().getText(), is(animal.getStory().getText()));
    }

    private void assertEqualsPrimaryLinkPicture(
            final LinkPicture primaryLinkPicture, final LinkPicture expectedPrimaryLinkPicture
    ) {
        assertThat(primaryLinkPicture.getName(), is(expectedPrimaryLinkPicture.getName()));
        assertThat(primaryLinkPicture.getPictureType(), is(expectedPrimaryLinkPicture.getPictureType()));
        assertThat(primaryLinkPicture.getLargeImageUrl(), is(expectedPrimaryLinkPicture.getLargeImageUrl()));
        assertThat(primaryLinkPicture.getSmallImageUrl(), is(expectedPrimaryLinkPicture.getSmallImageUrl()));
    }

    private void assertEqualsCharacteristics(
            final Characteristics characteristics, final Characteristics expectedCharacteristics
    ) {
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
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaAnimal.class).usingGetClass()
                      .withPrefabValues(JpaPrimaryLinkPicture.class, mock(JpaPrimaryLinkPicture.class), mock(JpaPrimaryLinkPicture.class))
                      .withPrefabValues(JpaCharacteristics.class, mock(JpaCharacteristics.class), mock(JpaCharacteristics.class))
                      .withPrefabValues(JpaStory.class, mock(JpaStory.class), mock(JpaStory.class))
                      .suppress(Warning.SURROGATE_KEY)
                      .verify();
    }
}