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

package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Story;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.models.jpa.JpaAnimal;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaAnimalRepositoryIntegrationTest extends AbstractJpaRepositoryIntegrationTest {

    @Test
    public void shouldSaveAnAnimal() {
        JpaAnimal entity = new JpaAnimal(AnimalBuilder.random().build());
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);

        assertReflectionEquals(jpaAnimal, entity);
    }

    @Test
    public void shouldSavePrimaryLinkPictureForAnimal() {
        JpaAnimal jpaAnimalWithNoPrimaryLinkPicture = createAndSaveJpaAnimal();
        Animal animalWithNoPrimaryLinkPicture = jpaAnimalWithNoPrimaryLinkPicture.toAnimal();
        assertThat(animalWithNoPrimaryLinkPicture.getPrimaryLinkPicture(), is(nullValue()));
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        animalWithNoPrimaryLinkPicture.setPrimaryLinkPicture(primaryLinkPicture);
        JpaAnimal entity = new JpaAnimal(animalWithNoPrimaryLinkPicture);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithPrimaryLinkPicture = jpaAnimal.toAnimal();

        assertReflectionEquals(primaryLinkPicture, animalWithPrimaryLinkPicture.getPrimaryLinkPicture());
    }

    @Test
    public void shouldSaveCharacteristicsForAnimal() {
        JpaAnimal jpaAnimalWithNoCharacteristics = createAndSaveJpaAnimal();
        Animal animalWithNoCharacteristics = jpaAnimalWithNoCharacteristics.toAnimal();
        assertThat(animalWithNoCharacteristics.getCharacteristics(), is(nullValue()));
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        animalWithNoCharacteristics.setCharacteristics(characteristics);
        JpaAnimal entity = new JpaAnimal(animalWithNoCharacteristics);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithCharacteristics = jpaAnimal.toAnimal();

        assertReflectionEquals(characteristics, animalWithCharacteristics.getCharacteristics());
    }

    @Test
    public void shouldSaveStoryForAnimal() {
        JpaAnimal jpaAnimalWithNoStory = createAndSaveJpaAnimal();
        Animal animalWithNoStory = jpaAnimalWithNoStory.toAnimal();
        assertThat(animalWithNoStory.getStory(), is(nullValue()));
        Story story = new Story(randomAlphabetic(10));
        animalWithNoStory.setStory(story);
        JpaAnimal entity = new JpaAnimal(animalWithNoStory);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithStory = jpaAnimal.toAnimal();

        assertReflectionEquals(story, animalWithStory.getStory());
    }

    @Test
    public void shouldFindAnimalByAnimalUuid() {
        JpaAnimal entity = new JpaAnimal(AnimalBuilder.random().build());
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();

        Optional<JpaAnimal> optionalJpaAnimal = jpaAnimalRepository.findById(animalUuid);

        assertThat(optionalJpaAnimal.isPresent(), is(true));
        assertReflectionEquals(optionalJpaAnimal.get(), jpaAnimal);
    }

    @Test
    public void shouldFindAllAnimals() {
        deleteAllJpaAnimals();
        JpaAnimal firstJpaAnimal = new JpaAnimal(AnimalBuilder.random().build());
        JpaAnimal secondJpaAnimal = new JpaAnimal(AnimalBuilder.random().build());
        JpaAnimal thirdJpaAnimal = new JpaAnimal(AnimalBuilder.random().build());
        ArrayList<JpaAnimal> expectedJpaAnimals = newArrayList(firstJpaAnimal, secondJpaAnimal, thirdJpaAnimal);
        expectedJpaAnimals.forEach(jpaAnimal -> jpaAnimalRepository.save(jpaAnimal));

        assertReflectionEquals(expectedJpaAnimals, jpaAnimalRepository.findAll());
    }

    @Test
    public void shouldReturnPageWithAllAnimalsByLookingForHumanStateName() {
        deleteAllJpaAnimals();
        State lookingForHuman = new LookingForHuman(LocalDateTime.now());
        List<JpaAnimal> expectedJpaAnimalsWithLookingForHumanState = newArrayList(
                new JpaAnimal(AnimalBuilder.random().withState(lookingForHuman).build()),
                new JpaAnimal(AnimalBuilder.random().withState(lookingForHuman).build()),
                new JpaAnimal(AnimalBuilder.random().withState(lookingForHuman).build())
        );
        expectedJpaAnimalsWithLookingForHumanState.forEach(jpaAnimal -> jpaAnimalRepository.save(jpaAnimal));
        IntStream.rangeClosed(1, 10).forEach(n -> {
            State anotherState = new Unavailable(LocalDateTime.now(), randomAlphabetic(10));
            JpaAnimal jpaAnimalWithAnotherState = new JpaAnimal(AnimalBuilder.random().withState(anotherState).build());
            jpaAnimalRepository.save(jpaAnimalWithAnotherState);
        });
        Pageable pageable = PageRequest.of(0, 3);
        Page<JpaAnimal> expectedPageOfJpaAnimals = new PageImpl<>(
                expectedJpaAnimalsWithLookingForHumanState, pageable, 3
        );

        Page<JpaAnimal> allByStateName = jpaAnimalRepository.findAllByStateName(
                lookingForHuman.getStateName(), pageable
        );

        assertReflectionEquals(expectedPageOfJpaAnimals, allByStateName);
    }
}