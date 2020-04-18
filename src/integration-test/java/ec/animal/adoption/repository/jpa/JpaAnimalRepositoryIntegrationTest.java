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

package ec.animal.adoption.repository.jpa;

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.builders.StoryBuilder;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.story.Story;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.model.jpa.JpaAnimal;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@SuppressWarnings({"PMD.ExcessiveImports", "PMD.TooManyMethods"})
public class JpaAnimalRepositoryIntegrationTest extends AbstractJpaRepositoryIntegrationTest {

    @Test
    public void shouldSaveAJpaAnimal() {
        JpaAnimal entity = new JpaAnimal(AnimalBuilder.random().build());
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);

        assertReflectionEquals(jpaAnimal, entity);
    }

    @Test
    public void shouldSaveJpaAnimalWithJpaPrimaryLinkPicture() {
        LinkPicture expectedPrimaryLinkPicture = LinkPictureBuilder.random()
                                                                   .withPictureType(PictureType.PRIMARY).build();
        Animal animal = AnimalBuilder.random().withPrimaryLinkPicture(expectedPrimaryLinkPicture).build();
        JpaAnimal entity = new JpaAnimal(animal);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithPrimaryLinkPicture = jpaAnimal.toAnimal();
        LinkPicture primaryLinkPicture = animalWithPrimaryLinkPicture.getPrimaryLinkPicture();

        assertThat(primaryLinkPicture.getName(), is(expectedPrimaryLinkPicture.getName()));
        assertThat(primaryLinkPicture.getPictureType(), is(expectedPrimaryLinkPicture.getPictureType()));
        assertThat(primaryLinkPicture.getLargeImageUrl(), is(expectedPrimaryLinkPicture.getLargeImageUrl()));
        assertThat(primaryLinkPicture.getSmallImageUrl(), is(expectedPrimaryLinkPicture.getSmallImageUrl()));
    }

    @Test
    public void shouldSaveJpaAnimalWithJpaCharacteristics() {
        Characteristics expectedCharacteristics = CharacteristicsBuilder.random().build();
        Animal animal = AnimalBuilder.random().withCharacteristics(expectedCharacteristics).build();
        JpaAnimal entity = new JpaAnimal(animal);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithCharacteristics = jpaAnimal.toAnimal();
        Characteristics characteristics = animalWithCharacteristics.getCharacteristics();

        assertThat(characteristics.getSize(), is(expectedCharacteristics.getSize()));
        assertThat(characteristics.getPhysicalActivity(), is(expectedCharacteristics.getPhysicalActivity()));
        assertThat(characteristics.getFriendlyWith(), is(expectedCharacteristics.getFriendlyWith()));
        assertThat(characteristics.getTemperaments(), is(expectedCharacteristics.getTemperaments()));
    }

    @Test
    public void shouldSaveJpaAnimalWithJpaStory() {
        Story expectedStory = StoryBuilder.random().build();
        Animal animal = AnimalBuilder.random().withStory(expectedStory).build();
        JpaAnimal entity = new JpaAnimal(animal);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithStory = jpaAnimal.toAnimal();
        Story story = animalWithStory.getStory();

        assertThat(story.getText(), is(expectedStory.getText()));
    }

    @Test
    public void shouldAddJpaPrimaryLinkPictureToJpaAnimal() {
        JpaAnimal jpaAnimalWithNoPrimaryLinkPicture = createAndSaveJpaAnimal();
        Animal animalWithNoPrimaryLinkPicture = jpaAnimalWithNoPrimaryLinkPicture.toAnimal();
        assertThat(animalWithNoPrimaryLinkPicture.getPrimaryLinkPicture(), is(nullValue()));
        LinkPicture expectedPrimaryLinkPicture = LinkPictureBuilder.random()
                                                                   .withPictureType(PictureType.PRIMARY).build();
        animalWithNoPrimaryLinkPicture.setPrimaryLinkPicture(expectedPrimaryLinkPicture);
        JpaAnimal entity = new JpaAnimal(animalWithNoPrimaryLinkPicture);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithPrimaryLinkPicture = jpaAnimal.toAnimal();
        LinkPicture primaryLinkPicture = animalWithPrimaryLinkPicture.getPrimaryLinkPicture();

        assertThat(primaryLinkPicture.getName(), is(expectedPrimaryLinkPicture.getName()));
        assertThat(primaryLinkPicture.getPictureType(), is(expectedPrimaryLinkPicture.getPictureType()));
        assertThat(primaryLinkPicture.getLargeImageUrl(), is(expectedPrimaryLinkPicture.getLargeImageUrl()));
        assertThat(primaryLinkPicture.getSmallImageUrl(), is(expectedPrimaryLinkPicture.getSmallImageUrl()));
    }

    @Test
    public void shouldAddJpaCharacteristicsToJpaAnimal() {
        JpaAnimal jpaAnimalWithNoCharacteristics = createAndSaveJpaAnimal();
        Animal animalWithNoCharacteristics = jpaAnimalWithNoCharacteristics.toAnimal();
        assertThat(animalWithNoCharacteristics.getCharacteristics(), is(nullValue()));
        Characteristics expectedCharacteristics = CharacteristicsBuilder.random().build();
        animalWithNoCharacteristics.setCharacteristics(expectedCharacteristics);
        JpaAnimal entity = new JpaAnimal(animalWithNoCharacteristics);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithCharacteristics = jpaAnimal.toAnimal();
        Characteristics characteristics = animalWithCharacteristics.getCharacteristics();

        assertThat(characteristics.getSize(), is(expectedCharacteristics.getSize()));
        assertThat(characteristics.getPhysicalActivity(), is(expectedCharacteristics.getPhysicalActivity()));
        assertThat(characteristics.getFriendlyWith(), is(expectedCharacteristics.getFriendlyWith()));
        assertThat(characteristics.getTemperaments(), is(expectedCharacteristics.getTemperaments()));
    }

    @Test
    public void shouldAddJpaStoryToJpaAnimal() {
        JpaAnimal jpaAnimalWithNoStory = createAndSaveJpaAnimal();
        Animal animalWithNoStory = jpaAnimalWithNoStory.toAnimal();
        assertThat(animalWithNoStory.getStory(), is(nullValue()));
        Story expectedStory = StoryBuilder.random().build();
        animalWithNoStory.setStory(expectedStory);
        JpaAnimal entity = new JpaAnimal(animalWithNoStory);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithStory = jpaAnimal.toAnimal();
        Story story = animalWithStory.getStory();

        assertThat(story.getText(), is(expectedStory.getText()));
    }

    @Test
    public void shouldFindJpaAnimalByAnimalUuid() {
        JpaAnimal entity = new JpaAnimal(AnimalBuilder.random().build());
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();

        Optional<JpaAnimal> optionalJpaAnimal = jpaAnimalRepository.findById(animalUuid);

        assertThat(optionalJpaAnimal.isPresent(), is(true));
        assertThat(optionalJpaAnimal.get(), is(jpaAnimal));
    }

    @Test
    public void shouldReturnPageWithFourJpaAnimals() {
        IntStream.rangeClosed(1, 4).forEach(n -> jpaAnimalRepository.save(new JpaAnimal(AnimalBuilder.random().build())));
        Pageable pageable = PageRequest.of(0, 4);

        Page<JpaAnimal> pageOfJpaAnimals = jpaAnimalRepository.findAll(pageable);
        List<JpaAnimal> jpaAnimals = pageOfJpaAnimals.get().collect(toList());

        assertThat(pageOfJpaAnimals.getPageable(), is(pageable));
        assertThat(jpaAnimals.size(), is(4));
    }

    @Test
    public void shouldReturnPageWithEightJpaAnimalsWithMatchingStateAndSpeciesAndMatchingAllFilters() {
        State lookingForHuman = new LookingForHuman(LocalDateTime.now());
        Species dog = Species.DOG;
        PhysicalActivity high = PhysicalActivity.HIGH;
        Size tiny = Size.TINY;
        IntStream.rangeClosed(1, 8).forEach(i -> jpaAnimalRepository.save(getJpaAnimalWith(lookingForHuman, dog, high, tiny)));
        saveOtherJpaAnimalsWithDifferentState(new Adopted(LocalDateTime.now(), randomAlphabetic(10)));
        Pageable pageable = PageRequest.of(0, 8);

        Page<JpaAnimal> pageOfJpaAnimals = jpaAnimalRepository
                .findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
                        lookingForHuman.getStateName(), dog.name(), high.name(), tiny.name(), pageable
                );
        List<JpaAnimal> jpaAnimals = pageOfJpaAnimals.get().collect(toList());

        assertThat(pageOfJpaAnimals.getPageable(), is(pageable));
        assertThat(jpaAnimals.size(), is(8));
    }

    @Test
    public void shouldReturnPageWithTwentyJpaAnimalsWithMatchingStateAndSpeciesAndMatchingSomeOrAllFilters() {
        State adopted = new Adopted(LocalDateTime.now(), randomAlphabetic(10));
        Species cat = Species.CAT;
        PhysicalActivity low = PhysicalActivity.LOW;
        Size medium = Size.MEDIUM;
        IntStream.rangeClosed(1, 5).forEach(i -> {
            jpaAnimalRepository.save(getJpaAnimalWith(adopted, cat, low, medium));
            jpaAnimalRepository.save(getJpaAnimalWith(adopted, cat, PhysicalActivity.HIGH, medium));
            jpaAnimalRepository.save(getJpaAnimalWith(adopted, cat, low, Size.OUTSIZE));
            jpaAnimalRepository.save(getJpaAnimalWith(adopted, cat, PhysicalActivity.MEDIUM, Size.SMALL));
        });
        saveOtherJpaAnimalsWithDifferentState(new Unavailable(LocalDateTime.now(), randomAlphabetic(10)));
        Pageable pageable = PageRequest.of(0, 20);

        Page<JpaAnimal> pageOfJpaAnimals = jpaAnimalRepository
                .findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
                        adopted.getStateName(), cat.name(), low.name(), medium.name(), pageable
                );
        List<JpaAnimal> jpaAnimals = pageOfJpaAnimals.get().collect(toList());

        assertThat(pageOfJpaAnimals.getPageable(), is(pageable));
        assertThat(jpaAnimals.size(), is(20));
    }

    private JpaAnimal getJpaAnimalWith(final State state,
                                       final Species species,
                                       final PhysicalActivity physicalActivity,
                                       final Size size) {
        return new JpaAnimal(
                AnimalBuilder.random().withState(state).withSpecies(species).withCharacteristics(
                        CharacteristicsBuilder.random().withPhysicalActivity(physicalActivity).withSize(size).build()
                ).build());
    }

    private void saveOtherJpaAnimalsWithDifferentState(final State state) {
        IntStream.rangeClosed(1, 10).forEach(n -> jpaAnimalRepository.save(
                new JpaAnimal(AnimalBuilder.random().withState(state).build())
        ));
    }
}