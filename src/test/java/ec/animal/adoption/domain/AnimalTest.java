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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class AnimalTest {

    private static final String ANIMAL_NAME_IS_REQUIRED = "Animal name is required";
    private static final String ANIMAL_CLINICAL_RECORD_IS_REQUIRED = "Animal clinical record is required";
    private static final String ANIMAL_SPECIES_IS_REQUIRED = "Animal species is required";
    private static final String ANIMAL_REGISTRATION_DATE_IS_REQUIRED = "Animal registration date is required";
    private static final String ANIMAL_ESTIMATED_AGE_IS_REQUIRED = "Animal estimated age is required";
    private static final String ANIMAL_SEX_IS_REQUIRED = "Animal sex is required";

    @Test
    public void shouldCreateAnimalWithNullUuidWhenUuidIsNotSet() {
        Animal animal = AnimalBuilder.random().build();

        assertNull(animal.getUuid());
    }

    @Test
    public void shouldSetUuid() {
        Animal animal = AnimalBuilder.random().build();
        UUID uuid = UUID.randomUUID();
        animal.setUuid(uuid);

        assertThat(animal.getUuid(), is(uuid));
    }

    @Test
    public void shouldCreateAnimalWithDefaultLookingForHumanStateWhenStateIsNull() {
        Animal animal = AnimalBuilder.random().withState(null).build();
        LookingForHuman expectedLookingForHumanState = new LookingForHuman(animal.getRegistrationDate());

        assertReflectionEquals(animal.getState(), expectedLookingForHumanState);
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Animal.class).usingGetClass().withNonnullFields("state")
                .suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithDefaultLookingForHumanState() throws IOException {
        Animal animal = AnimalBuilder.random().withState(null).build();
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedAnimalForAdoption = objectMapper.writeValueAsString(animal);
        Animal deserializedAnimalForAdoption = objectMapper.readValue(
                serializedAnimalForAdoption, Animal.class
        );

        assertReflectionEquals(deserializedAnimalForAdoption, animal);
        assertThat(deserializedAnimalForAdoption.getState(), is(notNullValue()));
        assertThat(deserializedAnimalForAdoption.getState(), is(instanceOf(LookingForHuman.class)));
        assertReflectionEquals(deserializedAnimalForAdoption.getState(), animal.getState());
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithAdoptedState() throws IOException {
        State adoptedState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        Animal animal = AnimalBuilder.random().withState(adoptedState).build();
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedAdoptedAnimal = objectMapper.writeValueAsString(animal);
        Animal deserializedAdoptedAnimal = objectMapper.readValue(
                serializedAdoptedAnimal, Animal.class
        );

        assertReflectionEquals(animal, deserializedAdoptedAnimal);
    }

    @Test
    public void shouldBeSerializableAndDeserializableWithUnavailableState() throws IOException {
        Unavailable unavailableState = new Unavailable(randomAlphabetic(10));
        Animal animal = AnimalBuilder.random().withState(unavailableState).build();
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedUnavailableAnimal = objectMapper.writeValueAsString(animal);
        Animal deserializedUnavailableAnimal = objectMapper.readValue(
                serializedUnavailableAnimal, Animal.class
        );

        assertThat(deserializedUnavailableAnimal, is(animal));
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
    public void shouldValidateNonNullRegistrationDate() {
        Animal animal = AnimalBuilder.random().withRegistrationDate(null).build();

        Set<ConstraintViolation<Animal>> constraintViolations = getValidator().validate(animal);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Animal> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_REGISTRATION_DATE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("registrationDate"));
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