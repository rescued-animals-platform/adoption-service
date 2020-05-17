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

import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalBuilder;
import ec.animal.adoption.domain.animal.AnimalFactory;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.LinkPictureFactory;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.story.Story;
import ec.animal.adoption.domain.story.StoryFactory;
import ec.animal.adoption.repository.jpa.model.JpaAnimal;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static ec.animal.adoption.domain.organization.OrganizationFactory.DEFAULT_ORGANIZATION_ID;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JpaAnimalRepositoryIntegrationTest extends AbstractJpaRepositoryIntegrationTest {

    @Test
    public void shouldSaveAJpaAnimal() {
        JpaAnimal entity = new JpaAnimal(AnimalFactory.randomWithDefaultOrganization().build());
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);

        assertEquals(jpaAnimal, entity);
    }

    @Test
    public void shouldSaveJpaAnimalWithJpaPrimaryLinkPicture() {
        LinkPicture expectedPrimaryLinkPicture = LinkPictureFactory.random()
                                                                   .withPictureType(PictureType.PRIMARY).build();
        Animal animal = AnimalFactory.randomWithDefaultOrganization()
                                     .withPrimaryLinkPicture(expectedPrimaryLinkPicture)
                                     .build();
        JpaAnimal entity = new JpaAnimal(animal);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithPrimaryLinkPicture = jpaAnimal.toAnimal();
        Optional<LinkPicture> primaryLinkPicture = animalWithPrimaryLinkPicture.getPrimaryLinkPicture();

        assertTrue(primaryLinkPicture.isPresent());
        assertThat(primaryLinkPicture.get().getName(), is(expectedPrimaryLinkPicture.getName()));
        assertThat(primaryLinkPicture.get().getPictureType(), is(expectedPrimaryLinkPicture.getPictureType()));
        assertThat(primaryLinkPicture.get().getLargeImageUrl(), is(expectedPrimaryLinkPicture.getLargeImageUrl()));
        assertThat(primaryLinkPicture.get().getSmallImageUrl(), is(expectedPrimaryLinkPicture.getSmallImageUrl()));
    }

    @Test
    public void shouldSaveJpaAnimalWithJpaCharacteristics() {
        Characteristics expectedCharacteristics = CharacteristicsFactory.random().build();
        Animal animal = AnimalFactory.randomWithDefaultOrganization()
                                     .withCharacteristics(expectedCharacteristics)
                                     .build();
        JpaAnimal entity = new JpaAnimal(animal);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithCharacteristics = jpaAnimal.toAnimal();
        Optional<Characteristics> characteristics = animalWithCharacteristics.getCharacteristics();

        assertTrue(characteristics.isPresent());
        assertThat(characteristics.get().getSize(), is(expectedCharacteristics.getSize()));
        assertThat(characteristics.get().getPhysicalActivity(), is(expectedCharacteristics.getPhysicalActivity()));
        assertThat(characteristics.get().getFriendlyWith(), is(expectedCharacteristics.getFriendlyWith()));
        assertThat(characteristics.get().getTemperaments(), is(expectedCharacteristics.getTemperaments()));
    }

    @Test
    public void shouldSaveJpaAnimalWithJpaStory() {
        Story expectedStory = StoryFactory.random().build();
        Animal animal = AnimalFactory.randomWithDefaultOrganization().withStory(expectedStory).build();
        JpaAnimal entity = new JpaAnimal(animal);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal animalWithStory = jpaAnimal.toAnimal();
        Optional<Story> story = animalWithStory.getStory();

        assertTrue(story.isPresent());
        assertThat(story.get().getText(), is(expectedStory.getText()));
    }

    @Test
    public void shouldAddJpaPrimaryLinkPictureToJpaAnimal() {
        JpaAnimal jpaAnimalWithNoPrimaryLinkPicture = createAndSaveJpaAnimalForDefaultOrganization();
        Animal animalWithNoPrimaryLinkPicture = jpaAnimalWithNoPrimaryLinkPicture.toAnimal();
        assertTrue(animalWithNoPrimaryLinkPicture.getPrimaryLinkPicture().isEmpty());
        LinkPicture expectedPrimaryLinkPicture = LinkPictureFactory.random()
                                                                   .withPictureType(PictureType.PRIMARY)
                                                                   .build();
        Animal animalWithPrimaryLinkPicture = AnimalBuilder.copyOf(animalWithNoPrimaryLinkPicture)
                                                           .with(expectedPrimaryLinkPicture)
                                                           .build();
        JpaAnimal entity = new JpaAnimal(animalWithPrimaryLinkPicture);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal savedAnimal = jpaAnimal.toAnimal();
        Optional<LinkPicture> primaryLinkPicture = savedAnimal.getPrimaryLinkPicture();

        assertTrue(primaryLinkPicture.isPresent());
        assertThat(primaryLinkPicture.get().getName(), is(expectedPrimaryLinkPicture.getName()));
        assertThat(primaryLinkPicture.get().getPictureType(), is(expectedPrimaryLinkPicture.getPictureType()));
        assertThat(primaryLinkPicture.get().getLargeImageUrl(), is(expectedPrimaryLinkPicture.getLargeImageUrl()));
        assertThat(primaryLinkPicture.get().getSmallImageUrl(), is(expectedPrimaryLinkPicture.getSmallImageUrl()));
    }

    @Test
    public void shouldAddJpaCharacteristicsToJpaAnimal() {
        JpaAnimal jpaAnimalWithNoCharacteristics = createAndSaveJpaAnimalForDefaultOrganization();
        Animal animalWithNoCharacteristics = jpaAnimalWithNoCharacteristics.toAnimal();
        assertTrue(animalWithNoCharacteristics.getCharacteristics().isEmpty());
        Characteristics expectedCharacteristics = CharacteristicsFactory.random().build();
        Animal animalWithCharacteristics = AnimalBuilder.copyOf(animalWithNoCharacteristics)
                                                        .with(expectedCharacteristics)
                                                        .build();
        JpaAnimal entity = new JpaAnimal(animalWithCharacteristics);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal savedAnimal = jpaAnimal.toAnimal();
        Optional<Characteristics> characteristics = savedAnimal.getCharacteristics();

        assertTrue(characteristics.isPresent());
        assertThat(characteristics.get().getSize(), is(expectedCharacteristics.getSize()));
        assertThat(characteristics.get().getPhysicalActivity(), is(expectedCharacteristics.getPhysicalActivity()));
        assertThat(characteristics.get().getFriendlyWith(), is(expectedCharacteristics.getFriendlyWith()));
        assertThat(characteristics.get().getTemperaments(), is(expectedCharacteristics.getTemperaments()));
    }

    @Test
    public void shouldAddJpaStoryToJpaAnimal() {
        JpaAnimal jpaAnimalWithNoStory = createAndSaveJpaAnimalForDefaultOrganization();
        Animal animalWithNoStory = jpaAnimalWithNoStory.toAnimal();
        assertTrue(animalWithNoStory.getStory().isEmpty());
        Story expectedStory = StoryFactory.random().build();
        Animal animalWithStory = AnimalBuilder.copyOf(animalWithNoStory)
                                              .with(expectedStory)
                                              .build();
        JpaAnimal entity = new JpaAnimal(animalWithStory);

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        Animal savedAnimal = jpaAnimal.toAnimal();
        Optional<Story> story = savedAnimal.getStory();

        assertTrue(story.isPresent());
        assertThat(story.get().getText(), is(expectedStory.getText()));
    }

    @Test
    public void shouldFindJpaAnimalByAnimalIdAndOrganizationId() {
        JpaAnimal jpaAnimal = createAndSaveJpaAnimalForDefaultOrganization();
        UUID animalId = jpaAnimal.toAnimal().getIdentifier();

        Optional<JpaAnimal> optionalJpaAnimal = jpaAnimalRepository.findByIdAndJpaOrganizationId(
                animalId, DEFAULT_ORGANIZATION_ID
        );

        assertThat(optionalJpaAnimal.isPresent(), is(true));
        assertThat(optionalJpaAnimal.get(), is(jpaAnimal));
    }

    @Test
    public void shouldReturnPageWithFourJpaAnimalsMatchingOrganizationId() {
        IntStream.rangeClosed(1, 5).forEach(n -> createAndSaveJpaAnimalForDefaultOrganization());
        IntStream.rangeClosed(1, 5).forEach(n -> createAndSaveJpaAnimalForAnotherOrganization());
        Pageable pageable = PageRequest.of(0, 4);

        Page<JpaAnimal> pageOfJpaAnimals = jpaAnimalRepository.findAllByJpaOrganizationId(
                DEFAULT_ORGANIZATION_ID, pageable
        );
        List<JpaAnimal> jpaAnimals = pageOfJpaAnimals.get().collect(toList());

        assertEquals(pageable, pageOfJpaAnimals.getPageable());
        assertEquals(4, jpaAnimals.size());
        jpaAnimals.forEach(jpaAnimal -> {
            Animal animal = jpaAnimal.toAnimal();
            assertEquals(DEFAULT_ORGANIZATION_ID, animal.getOrganizationId());
        });
    }

    @Test
    void shouldReturnTrueWhenJpaAnimalMatchingClinicalRecordAndOrganizationIdExists() {
        JpaAnimal expectedJpaAnimal = createAndSaveJpaAnimalForDefaultOrganization();
        Animal animal = expectedJpaAnimal.toAnimal();

        boolean exists = jpaAnimalRepository.existsByClinicalRecordAndJpaOrganizationId(
                animal.getClinicalRecord(), animal.getOrganizationId()
        );

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenJpaAnimalMatchingClinicalRecordAndOrganizationIdDoesNotExist() {
        boolean exists = jpaAnimalRepository.existsByClinicalRecordAndJpaOrganizationId(
                randomAlphabetic(10), UUID.randomUUID()
        );

        assertFalse(exists);
    }

    @Test
    public void shouldReturnPageWithEightJpaAnimalsWithMatchingStateAndSpeciesAndMatchingAllFilters() {
        State lookingForHuman = State.lookingForHuman();
        Species dog = Species.DOG;
        PhysicalActivity high = PhysicalActivity.HIGH;
        Size tiny = Size.TINY;
        IntStream.rangeClosed(1, 8).forEach(i -> jpaAnimalRepository.save(getJpaAnimalWith(lookingForHuman, dog, high, tiny)));
        saveOtherJpaAnimalsWithDifferentState(State.adopted(randomAlphabetic(10)));
        Pageable pageable = PageRequest.of(0, 8);

        Page<JpaAnimal> pageOfJpaAnimals = jpaAnimalRepository
                .findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
                        lookingForHuman.getName().name(), dog.name(), high.name(), tiny.name(), pageable
                );
        List<JpaAnimal> jpaAnimals = pageOfJpaAnimals.get().collect(toList());

        assertThat(pageOfJpaAnimals.getPageable(), is(pageable));
        assertThat(jpaAnimals.size(), is(8));
    }

    @Test
    public void shouldReturnPageWithTwentyJpaAnimalsWithMatchingStateAndSpeciesAndMatchingSomeOrAllFilters() {
        State adopted = State.adopted(randomAlphabetic(10));
        Species cat = Species.CAT;
        PhysicalActivity low = PhysicalActivity.LOW;
        Size medium = Size.MEDIUM;
        IntStream.rangeClosed(1, 5).forEach(i -> {
            jpaAnimalRepository.save(getJpaAnimalWith(adopted, cat, low, medium));
            jpaAnimalRepository.save(getJpaAnimalWith(adopted, cat, PhysicalActivity.HIGH, medium));
            jpaAnimalRepository.save(getJpaAnimalWith(adopted, cat, low, Size.OUTSIZE));
            jpaAnimalRepository.save(getJpaAnimalWith(adopted, cat, PhysicalActivity.MEDIUM, Size.SMALL));
        });
        saveOtherJpaAnimalsWithDifferentState(State.unavailable(randomAlphabetic(10)));
        Pageable pageable = PageRequest.of(0, 20);

        Page<JpaAnimal> pageOfJpaAnimals = jpaAnimalRepository
                .findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
                        adopted.getName().name(), cat.name(), low.name(), medium.name(), pageable
                );
        List<JpaAnimal> jpaAnimals = pageOfJpaAnimals.get().collect(toList());

        assertThat(pageOfJpaAnimals.getPageable(), is(pageable));
        assertThat(jpaAnimals.size(), is(20));
    }

    private JpaAnimal getJpaAnimalWith(final State state,
                                       final Species species,
                                       final PhysicalActivity physicalActivity,
                                       final Size size) {
        Characteristics characteristics = CharacteristicsFactory.random()
                                                                .withPhysicalActivity(physicalActivity)
                                                                .withSize(size)
                                                                .build();
        return new JpaAnimal(AnimalFactory.randomWithDefaultOrganization()
                                          .withState(state)
                                          .withSpecies(species)
                                          .withCharacteristics(characteristics)
                                          .build());
    }

    private void saveOtherJpaAnimalsWithDifferentState(final State state) {
        IntStream.rangeClosed(1, 10).forEach(n -> jpaAnimalRepository.save(
                new JpaAnimal(AnimalFactory.randomWithDefaultOrganization().withState(state).build())
        ));
    }
}