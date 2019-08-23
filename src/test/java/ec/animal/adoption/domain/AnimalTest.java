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

package ec.animal.adoption.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.builders.StoryBuilder;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomState;
import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.ExcessiveImports"})
public class AnimalTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final String ANIMAL_NAME_IS_REQUIRED = "Animal name is required";
    private static final String ANIMAL_CLINICAL_RECORD_IS_REQUIRED = "Animal clinical record is required";
    private static final String ANIMAL_SPECIES_IS_REQUIRED = "Animal species is required";
    private static final String ANIMAL_ESTIMATED_AGE_IS_REQUIRED = "Animal estimated age is required";
    private static final String ANIMAL_SEX_IS_REQUIRED = "Animal sex is required";
    private static final String CLINICAL_RECORD_JSON = "{\"clinicalRecord\":\"";
    private static final String NAME_JSON = "\",\"name\":\"";
    private static final String SPECIES_JSON = "\",\"species\":\"";
    private static final String ESTIMATED_AGE_JSON = "\",\"estimatedAge\":\"";
    private static final String SEX_JSON = "\",\"sex\":\"";

    private final ObjectMapper objectMapper = TestUtils.getObjectMapper();

    @Test
    public void shouldReturnStateName() {
        State randomState = getRandomState();
        Animal animal = AnimalBuilder.random().withState(randomState).build();

        assertThat(animal.getStateName(), is(randomState.getStateName()));
    }

    @Test
    public void shouldSetPrimaryLinkPicture() {
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        Animal animal = AnimalBuilder.random().build();

        animal.setPrimaryLinkPicture(primaryLinkPicture);

        assertThat(animal.getPrimaryLinkPicture(), is(primaryLinkPicture));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenSettingAnAlternateLinkPicture() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Picture type should be PRIMARY");

        Animal animal = AnimalBuilder.random().build();
        LinkPicture alternateLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.ALTERNATE).build();

        animal.setPrimaryLinkPicture(alternateLinkPicture);
    }

    @Test
    public void shouldSetCharacteristics() {
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        Animal animal = AnimalBuilder.random().build();

        animal.setCharacteristics(characteristics);

        assertThat(animal.getCharacteristics(), is(characteristics));
    }

    @Test
    public void shouldSetStory() {
        Story story = StoryBuilder.random().build();
        Animal animal = AnimalBuilder.random().build();

        animal.setStory(story);

        assertThat(animal.getStory(), is(story));
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Animal.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

    @Test
    public void shouldSerializeAnimal() throws JsonProcessingException {
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        Story story = StoryBuilder.random().build();
        Animal animal = AnimalBuilder.random().withUuid(UUID.randomUUID()).withPrimaryLinkPicture(primaryLinkPicture)
                .withCharacteristics(characteristics).withStory(story).withRegistrationDate(LocalDateTime.now()).build();
        String expectedSerializedState = objectMapper.writeValueAsString(animal.getState());
        String expectedRegistrationDate = objectMapper.writeValueAsString(animal.getRegistrationDate());
        String expectedPrimaryLinkPicture = objectMapper.writeValueAsString(primaryLinkPicture);
        String expectedCharacteristics = objectMapper.writeValueAsString(characteristics);
        String expectedStory = objectMapper.writeValueAsString(story);

        String serializedAnimal = objectMapper.writeValueAsString(animal);

        assertThat(serializedAnimal, containsString(animal.getUuid().toString()));
        assertThat(serializedAnimal, containsString(expectedRegistrationDate));
        assertThat(serializedAnimal, containsString(animal.getClinicalRecord()));
        assertThat(serializedAnimal, containsString(animal.getName()));
        assertThat(serializedAnimal, containsString(animal.getSpecies().name()));
        assertThat(serializedAnimal, containsString(animal.getEstimatedAge().name()));
        assertThat(serializedAnimal, containsString(animal.getSex().name()));
        assertThat(serializedAnimal, containsString(expectedSerializedState));
        assertThat(serializedAnimal, containsString(expectedPrimaryLinkPicture));
        assertThat(serializedAnimal, containsString(expectedCharacteristics));
        assertThat(serializedAnimal, containsString(expectedStory));
    }

    @Test
    public void shouldDeserializeAnimalWithoutPrimaryLinkPicture() throws IOException {
        Animal animal = AnimalBuilder.random().build();
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        String primaryLinkPictureAsJson = objectMapper.writeValueAsString(primaryLinkPicture);
        String serializedAnimalWithPrimaryLinkPicture = CLINICAL_RECORD_JSON + animal.getClinicalRecord() +
                NAME_JSON + animal.getName() + SPECIES_JSON + animal.getSpecies().name() +
                ESTIMATED_AGE_JSON + animal.getEstimatedAge().name() + SEX_JSON +
                animal.getSex().name() + "\",\"primaryLinkPicture\":" + primaryLinkPictureAsJson + "}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithPrimaryLinkPicture, Animal.class);

        assertThat(deserializedAnimal.getPrimaryLinkPicture(), is(nullValue()));
    }

    @Test
    public void shouldDeserializeAnimalWithoutCharacteristics() throws IOException {
        Animal animal = AnimalBuilder.random().build();
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        String characteristicsAsJson = objectMapper.writeValueAsString(characteristics);
        String serializedAnimalWithCharacteristics = CLINICAL_RECORD_JSON + animal.getClinicalRecord() +
                NAME_JSON + animal.getName() + SPECIES_JSON + animal.getSpecies().name() +
                ESTIMATED_AGE_JSON + animal.getEstimatedAge().name() + SEX_JSON +
                animal.getSex().name() + "\",\"characteristics\":" + characteristicsAsJson + "}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithCharacteristics, Animal.class);

        assertThat(deserializedAnimal.getCharacteristics(), is(nullValue()));
    }

    @Test
    public void shouldDeserializeAnimalWithoutStory() throws IOException {
        Animal animal = AnimalBuilder.random().build();
        Story story = StoryBuilder.random().build();
        String storyAsJson = objectMapper.writeValueAsString(story);
        String serializedAnimalWithStory = CLINICAL_RECORD_JSON + animal.getClinicalRecord() +
                NAME_JSON + animal.getName() + SPECIES_JSON + animal.getSpecies().name() +
                ESTIMATED_AGE_JSON + animal.getEstimatedAge().name() + SEX_JSON +
                animal.getSex().name() + "\",\"story\":" + storyAsJson + "}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithStory, Animal.class);

        assertThat(deserializedAnimal.getStory(), is(nullValue()));
    }

    @Test
    public void shouldSerializeAnimalWithoutStateName() throws JsonProcessingException {
        State state = getRandomState();
        Animal animal = AnimalBuilder.random().withState(state).build();

        String serializedAnimal = objectMapper.writeValueAsString(animal);

        assertThat(serializedAnimal, not(containsString(String.format("\"stateName\":\"%s\"", state.getStateName()))));
    }

    @Test
    public void shouldDeserializeAnimalWithoutStateIntoAnAnimalWithLookingForHumanStateAsDefault() throws IOException {
        Animal animal = AnimalBuilder.random().build();
        String serializedAnimalWithoutState = CLINICAL_RECORD_JSON + animal.getClinicalRecord() +
                NAME_JSON + animal.getName() + SPECIES_JSON + animal.getSpecies().name() +
                ESTIMATED_AGE_JSON + animal.getEstimatedAge().name() + SEX_JSON +
                animal.getSex().name() + "\"}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithoutState, Animal.class);

        assertThat(deserializedAnimal.getClinicalRecord(), is(animal.getClinicalRecord()));
        assertThat(deserializedAnimal.getName(), is(animal.getName()));
        assertThat(deserializedAnimal.getSpecies(), is(animal.getSpecies()));
        assertThat(deserializedAnimal.getEstimatedAge(), is(animal.getEstimatedAge()));
        assertThat(deserializedAnimal.getSex(), is(animal.getSex()));
        assertThat(deserializedAnimal.getState(), is(instanceOf(LookingForHuman.class)));
    }

    @Test
    public void shouldDeserializeAnimalWithLookingForHumanState() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        Animal animal = AnimalBuilder.random().withState(new LookingForHuman(localDateTime)).build();
        String serializedAnimalWithState = CLINICAL_RECORD_JSON + animal.getClinicalRecord() +
                NAME_JSON + animal.getName() + SPECIES_JSON + animal.getSpecies().name() +
                ESTIMATED_AGE_JSON + animal.getEstimatedAge().name() + SEX_JSON + animal.getSex().name() +
                "\",\"state\":{\"lookingForHuman\":{\"date\":" + serializedLocalDateTime + "}}}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithState, Animal.class);

        assertReflectionEquals(animal, deserializedAnimal);
    }

    @Test
    public void shouldDeserializeAnimalWithUnavailableState() throws IOException {
        String notes = randomAlphabetic(20);
        LocalDateTime localDateTime = LocalDateTime.now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        Animal animal = AnimalBuilder.random().withState(new Unavailable(localDateTime, notes)).build();
        String serializedAnimalWithState = CLINICAL_RECORD_JSON + animal.getClinicalRecord() +
                NAME_JSON + animal.getName() + SPECIES_JSON + animal.getSpecies().name() +
                ESTIMATED_AGE_JSON + animal.getEstimatedAge().name() + SEX_JSON + animal.getSex().name() +
                "\",\"state\":{\"unavailable\":{\"notes\":\"" + notes +
                "\",\"date\":" + serializedLocalDateTime + "}}}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithState, Animal.class);

        assertReflectionEquals(animal, deserializedAnimal);
    }

    @Test
    public void shouldDeserializeAnimalWithAdoptedState() throws IOException {
        String adoptionFormId = randomAlphabetic(10);
        LocalDateTime localDateTime = LocalDateTime.now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        Animal animal = AnimalBuilder.random().withState(new Adopted(localDateTime, adoptionFormId)).build();
        String serializedAnimalWithState = CLINICAL_RECORD_JSON + animal.getClinicalRecord() +
                NAME_JSON + animal.getName() + SPECIES_JSON + animal.getSpecies().name() +
                ESTIMATED_AGE_JSON + animal.getEstimatedAge().name() + SEX_JSON + animal.getSex().name() +
                "\",\"state\":{\"adopted\":{\"adoptionFormId\":\"" + adoptionFormId +
                "\",\"date\":" + serializedLocalDateTime + "}}}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithState, Animal.class);

        assertReflectionEquals(animal, deserializedAnimal);
    }

    @Test
    public void shouldValidateNonNullClinicalRecord() {
        Animal animal = AnimalBuilder.random().withClinicalRecord(null).build();

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_CLINICAL_RECORD_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("clinicalRecord"));
    }

    @Test
    public void shouldValidateNonEmptyClinicalRecord() {
        Animal animal = AnimalBuilder.random().withClinicalRecord("").build();

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_CLINICAL_RECORD_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("clinicalRecord"));
    }

    @Test
    public void shouldValidateNonNullName() {
        Animal animal = AnimalBuilder.random().withName(null).build();

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonEmptyName() {
        Animal animal = AnimalBuilder.random().withName("").build();

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonNullSpecies() {
        Animal animal = AnimalBuilder.random().withSpecies(null).build();

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_SPECIES_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("species"));
    }

    @Test
    public void shouldValidateNonNullEstimatedAge() {
        Animal animal = AnimalBuilder.random().withEstimatedAge(null).build();

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_ESTIMATED_AGE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("estimatedAge"));
    }

    @Test
    public void shouldValidateNonNullSex() {
        Animal animal = AnimalBuilder.random().withSex(null).build();

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_SEX_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("sex"));
    }
}