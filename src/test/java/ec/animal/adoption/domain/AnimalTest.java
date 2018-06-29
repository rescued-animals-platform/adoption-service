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
import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.helpers.JsonHelper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class AnimalTest {

    private static final String ANIMAL_NAME_IS_REQUIRED = "Animal name is required";
    private static final String ANIMAL_CLINICAL_RECORD_IS_REQUIRED = "Animal clinical record is required";
    private static final String ANIMAL_SPECIES_IS_REQUIRED = "Animal species is required";
    private static final String ANIMAL_ESTIMATED_AGE_IS_REQUIRED = "Animal estimated age is required";
    private static final String ANIMAL_SEX_IS_REQUIRED = "Animal sex is required";

    private final ObjectMapper objectMapper = JsonHelper.getObjectMapper();

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Animal.class).usingGetClass().verify();
    }

    @Test
    public void shouldSerializeAnimal() throws JsonProcessingException {
        Animal animal = AnimalBuilder.random().withUuid(UUID.randomUUID()).build();
        String expectedSerializedState = objectMapper.writeValueAsString(animal.getState());
        String expectedRegistrationDate = objectMapper.writeValueAsString(animal.getRegistrationDate());

        String serializedAnimal = objectMapper.writeValueAsString(animal);

        assertThat(serializedAnimal, containsString(animal.getUuid().toString()));
        assertThat(serializedAnimal, containsString(expectedRegistrationDate));
        assertThat(serializedAnimal, containsString(animal.getClinicalRecord()));
        assertThat(serializedAnimal, containsString(animal.getName()));
        assertThat(serializedAnimal, containsString(animal.getSpecies().name()));
        assertThat(serializedAnimal, containsString(animal.getEstimatedAge().name()));
        assertThat(serializedAnimal, containsString(animal.getSex().name()));
        assertThat(serializedAnimal, containsString(expectedSerializedState));
    }

    @Test
    public void shouldDeserializeAnimalWithoutStateIntoAnAnimalWithLookingForHumanStateAsDefault() throws IOException {
        Animal animal = AnimalBuilder.random().build();
        String serializedAnimalWithoutState = "{\"clinicalRecord\":\"" + animal.getClinicalRecord() +
                "\",\"name\":\"" + animal.getName() + "\",\"species\":\"" + animal.getSpecies().name() +
                "\",\"estimatedAge\":\"" + animal.getEstimatedAge().name() + "\",\"sex\":\"" +
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
    public void shouldDeserializeAnimalWithUnavailableState() throws IOException {
        Animal animal = AnimalBuilder.random().build();
        String notes = randomAlphabetic(20);
        String serializedAnimalWithState = "{\"clinicalRecord\":\"" + animal.getClinicalRecord() +
                "\",\"name\":\"" + animal.getName() + "\",\"species\":\"" + animal.getSpecies().name() +
                "\",\"estimatedAge\":\"" + animal.getEstimatedAge().name() + "\",\"sex\":\"" + animal.getSex().name() +
                "\",\"state\":{\"unavailable\":{\"notes\":\"" + notes + "\"}}}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithState, Animal.class);

        assertThat(deserializedAnimal.getClinicalRecord(), is(animal.getClinicalRecord()));
        assertThat(deserializedAnimal.getName(), is(animal.getName()));
        assertThat(deserializedAnimal.getSpecies(), is(animal.getSpecies()));
        assertThat(deserializedAnimal.getEstimatedAge(), is(animal.getEstimatedAge()));
        assertThat(deserializedAnimal.getSex(), is(animal.getSex()));
        assertThat(deserializedAnimal.getState(), is(instanceOf(Unavailable.class)));
        Unavailable unavailable = (Unavailable) deserializedAnimal.getState();
        assertThat(unavailable.getNotes(), is(notes));
        assertNotNull(unavailable.getDate());
    }

    @Test
    public void shouldDeserializeAnimalWithAdoptedState() throws IOException {
        Animal animal = AnimalBuilder.random().build();
        String adoptionFormId = randomAlphabetic(10);
        String serializedAnimalWithState = "{\"clinicalRecord\":\"" + animal.getClinicalRecord() +
                "\",\"name\":\"" + animal.getName() + "\",\"species\":\"" + animal.getSpecies().name() +
                "\",\"estimatedAge\":\"" + animal.getEstimatedAge().name() + "\",\"sex\":\"" + animal.getSex().name() +
                "\",\"state\":{\"adopted\":{\"adoptionFormId\":\"" + adoptionFormId + "\"}}}";

        Animal deserializedAnimal = objectMapper.readValue(serializedAnimalWithState, Animal.class);

        assertThat(deserializedAnimal.getClinicalRecord(), is(animal.getClinicalRecord()));
        assertThat(deserializedAnimal.getName(), is(animal.getName()));
        assertThat(deserializedAnimal.getSpecies(), is(animal.getSpecies()));
        assertThat(deserializedAnimal.getEstimatedAge(), is(animal.getEstimatedAge()));
        assertThat(deserializedAnimal.getSex(), is(animal.getSex()));
        assertThat(deserializedAnimal.getState(), is(instanceOf(Adopted.class)));
        Adopted adopted = (Adopted) deserializedAnimal.getState();
        assertThat(adopted.getAdoptionFormId(), is(adoptionFormId));
        assertNotNull(adopted.getDate());
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