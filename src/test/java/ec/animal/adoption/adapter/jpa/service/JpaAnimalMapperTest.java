package ec.animal.adoption.adapter.jpa.service;

import ec.animal.adoption.adapter.jpa.model.*;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.AnimalFactory;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.animal.dto.AnimalDtoFactory;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.model.characteristics.FriendlyWith;
import ec.animal.adoption.domain.model.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.model.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.model.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.model.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.model.characteristics.temperaments.TemperamentsFactory;
import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.media.LinkPictureFactory;
import ec.animal.adoption.domain.model.media.PictureType;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.organization.OrganizationFactory;
import ec.animal.adoption.domain.model.state.State;
import ec.animal.adoption.domain.model.story.Story;
import ec.animal.adoption.domain.model.story.StoryFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomBalance;
import static ec.animal.adoption.TestUtils.getRandomDocility;
import static ec.animal.adoption.TestUtils.getRandomFriendlyWith;
import static ec.animal.adoption.TestUtils.getRandomSociability;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

class JpaAnimalMapperTest {

    @Test
    void shouldBuildAJpaAnimalFromAnAnimalDtoAndGenerateAnIdentifierAndARegistrationDateForIt() {
        Organization organization = OrganizationFactory.random().build();
        String adoptionFormId = randomAlphabetic(10);
        State state = State.adopted(adoptionFormId);
        AnimalDto animalDto = AnimalDtoFactory.random()
                                              .withState(state)
                                              .withOrganization(organization)
                                              .build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animalDto);

        assertAll(() -> assertNotNull(jpaAnimal.getId()),
                  () -> assertNotNull(jpaAnimal.getRegistrationDate()),
                  () -> assertEquals(animalDto.clinicalRecord(), jpaAnimal.getClinicalRecord()),
                  () -> assertEquals(animalDto.name(), jpaAnimal.getName()),
                  () -> assertEquals(animalDto.species().name(), jpaAnimal.getSpecies()),
                  () -> assertEquals(animalDto.estimatedAge().name(), jpaAnimal.getEstimatedAge()),
                  () -> assertEquals(animalDto.sex().name(), jpaAnimal.getSex()),
                  () -> assertEquals(state.getName().name(), jpaAnimal.getStateName()),
                  () -> assertEquals(adoptionFormId, jpaAnimal.getAdoptionFormId()),
                  () -> assertNull(jpaAnimal.getUnavailableStateNotes()),
                  () -> assertEquals(animalDto.organization().getOrganizationId(), jpaAnimal.getJpaOrganization().getId()),
                  () -> assertEquals(animalDto.organization().getName(), jpaAnimal.getJpaOrganization().getName()),
                  () -> assertEquals(animalDto.organization().getCity(), jpaAnimal.getJpaOrganization().getCity()),
                  () -> assertEquals(animalDto.organization().getAdoptionFormPdfUrl(), jpaAnimal.getJpaOrganization().getAdoptionFormPdfUrl()),
                  () -> assertEquals(animalDto.organization().getEmail(), jpaAnimal.getJpaOrganization().getEmail()),
                  () -> assertEquals(animalDto.organization().getReceptionAddress(), jpaAnimal.getJpaOrganization().getReceptionAddress())
        );
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

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(expectedAnimal);

        assertAll(() -> assertEquals(expectedAnimal.getIdentifier(), jpaAnimal.getId()),
                  () -> assertEquals(expectedAnimal.getRegistrationDate(), jpaAnimal.getRegistrationDate()),
                  () -> assertEquals(expectedAnimal.getClinicalRecord(), jpaAnimal.getClinicalRecord()),
                  () -> assertEquals(expectedAnimal.getName(), jpaAnimal.getName()),
                  () -> assertEquals(expectedAnimal.getSpecies().name(), jpaAnimal.getSpecies()),
                  () -> assertEquals(expectedAnimal.getEstimatedAge().name(), jpaAnimal.getEstimatedAge()),
                  () -> assertEquals(expectedAnimal.getSex().name(), jpaAnimal.getSex()),
                  () -> assertEquals(expectedAnimal.getState().getName().name(), jpaAnimal.getStateName()),
                  () -> assertNotNull(jpaAnimal.getJpaPrimaryLinkPicture()),
                  () -> assertNotNull(jpaAnimal.getJpaCharacteristics()),
                  () -> assertNotNull(jpaAnimal.getJpaStory()),
                  () -> assertNotNull(jpaAnimal.getJpaOrganization()));
    }

    @Test
    void shouldBuildAJpaPrimaryLinkPictureFromAnAnimal() {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        Animal animal = AnimalFactory.random().withPrimaryLinkPicture(primaryLinkPicture).build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);
        JpaPrimaryLinkPicture jpaPrimaryLinkPicture = jpaAnimal.getJpaPrimaryLinkPicture();

        assertThat(jpaPrimaryLinkPicture.getId(), is(primaryLinkPicture.getIdentifier()));
        assertThat(jpaPrimaryLinkPicture.getRegistrationDate(), is(primaryLinkPicture.getRegistrationDate()));
        assertThat(jpaPrimaryLinkPicture.getName(), is(primaryLinkPicture.getName()));
        assertThat(jpaPrimaryLinkPicture.getLargeImagePublicId(), is(primaryLinkPicture.getLargeImagePublicId()));
        assertThat(jpaPrimaryLinkPicture.getLargeImageUrl(), is(primaryLinkPicture.getLargeImageUrl()));
        assertThat(jpaPrimaryLinkPicture.getSmallImagePublicId(), is(primaryLinkPicture.getSmallImagePublicId()));
        assertThat(jpaPrimaryLinkPicture.getSmallImageUrl(), is(primaryLinkPicture.getSmallImageUrl()));
        assertThat(jpaPrimaryLinkPicture.getJpaAnimal(), is(jpaAnimal));
    }

    @Test
    void shouldBuildJpaCharacteristicsFromAnAnimal() {
        Sociability sociability = getRandomSociability();
        Docility docility = getRandomDocility();
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsFactory.random()
                                                       .withSociability(sociability)
                                                       .withDocility(docility)
                                                       .withBalance(balance)
                                                       .build();
        FriendlyWith friendlyWith = getRandomFriendlyWith();
        Characteristics characteristics = CharacteristicsFactory.random()
                                                                .withTemperaments(temperaments)
                                                                .withFriendlyWith(friendlyWith)
                                                                .build();
        Animal animal = AnimalFactory.random().withCharacteristics(characteristics).build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);
        JpaCharacteristics jpaCharacteristics = jpaAnimal.getJpaCharacteristics();

        assertThat(jpaCharacteristics.getId(), is(characteristics.getIdentifier()));
        assertThat(jpaCharacteristics.getRegistrationDate(), is(characteristics.getRegistrationDate()));
        assertThat(jpaCharacteristics.getSize(), is(characteristics.getSize().name()));
        assertThat(jpaCharacteristics.getSociability(), is(sociability.name()));
        assertThat(jpaCharacteristics.getDocility(), is(docility.name()));
        assertThat(jpaCharacteristics.getBalance(), is(balance.name()));
        assertThat(jpaCharacteristics.getFriendlyWith(), hasSize(1));
        assertThat(jpaCharacteristics.getFriendlyWith(), contains(new JpaFriendlyWith(friendlyWith.name())));
        assertThat(jpaCharacteristics.getPhysicalActivity(), is(characteristics.getPhysicalActivity().name()));
        assertThat(jpaCharacteristics.getJpaAnimal(), is(jpaAnimal));
    }

    @Test
    void shouldBuildAJpaStoryFromAnAnimal() {
        UUID storyId = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();
        String text = randomAlphabetic(100);
        Story story = StoryFactory.random()
                                  .withIdentifier(storyId)
                                  .withRegistrationDate(registrationDate)
                                  .withText(text)
                                  .build();
        Animal animal = AnimalFactory.random().withStory(story).build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);
        JpaStory jpaStory = jpaAnimal.getJpaStory();

        assertThat(jpaStory.getId(), is(storyId));
        assertThat(jpaStory.getRegistrationDate(), is(registrationDate));
        assertThat(jpaStory.getText(), is(text));
        assertThat(jpaStory.getJpaAnimal(), is(jpaAnimal));
    }

    @Test
    void shouldBuildAJpaOrganizationFromAnAnimal() {
        Organization organization = OrganizationFactory.random().build();
        Animal animal = AnimalFactory.random().withOrganization(organization).build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);
        JpaOrganization jpaOrganization = jpaAnimal.getJpaOrganization();

        assertAll(
                () -> assertEquals(jpaOrganization.getId(), organization.getOrganizationId()),
                () -> assertEquals(jpaOrganization.getName(), organization.getName()),
                () -> assertEquals(jpaOrganization.getCity(), organization.getCity()),
                () -> assertEquals(jpaOrganization.getEmail(), organization.getEmail()),
                () -> assertEquals(jpaOrganization.getReceptionAddress(), organization.getReceptionAddress()),
                () -> assertEquals(jpaOrganization.getAdoptionFormPdfUrl(), organization.getAdoptionFormPdfUrl())
        );
    }

    @Test
    void shouldBuildAJpaAnimalWithNoJpaPrimaryLinkPicture() {
        Animal animal = AnimalFactory.random().withPrimaryLinkPicture(null).build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);

        assertNull(jpaAnimal.getJpaPrimaryLinkPicture());
    }

    @Test
    void shouldBuildAJpaAnimalWithNoJpaCharacteristics() {
        Animal animal = AnimalFactory.random().withCharacteristics(null).build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);

        assertNull(jpaAnimal.getJpaCharacteristics());
    }

    @Test
    void shouldBuildAJpaAnimalWithNoJpaStory() {
        Animal animal = AnimalFactory.random().withStory(null).build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);

        assertNull(jpaAnimal.getJpaStory());
    }

    @Test
    void shouldBuildAJpaAnimalWithNoJpaOrganization() {
        Animal animal = AnimalFactory.random().withOrganization(null).build();

        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(animal);

        assertNull(jpaAnimal.getJpaOrganization());
    }

    @Test
    void shouldBuildAnAnimalFromAJpaAnimal() {
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
        JpaAnimal jpaAnimal = JpaAnimalMapper.MAPPER.toJpaAnimal(expectedAnimal);

        Animal actualAnimal = JpaAnimalMapper.MAPPER.toAnimal(jpaAnimal);

        assertReflectionEquals(expectedAnimal, actualAnimal);
    }
}