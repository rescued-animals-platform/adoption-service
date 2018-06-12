package ec.animal.adoption.resources;

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.AnimalSpecies;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static ec.animal.adoption.TestUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.*;

public class AnimalResourceIntegrationTest extends AbstractIntegrationTest {

    private String clinicalRecord;
    private String name;
    private LocalDateTime registrationDate;
    private AnimalSpecies animalSpecies;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private State state;

    @Before
    public void setUp() {
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        registrationDate = LocalDateTime.now();
        animalSpecies = getRandomType();
        estimatedAge = getRandomEstimatedAge();
        sex = getRandomSex();
        state = new LookingForHuman(registrationDate);
    }

    @Test
    public void shouldReturn201Created() {
        Animal animal = new Animal(
                clinicalRecord, name, registrationDate, animalSpecies, estimatedAge, sex, state
        );

        ResponseEntity<Animal> response = testClient.postForEntity(
                ANIMALS_URL, animal, Animal.class, getHttpHeaders()
        );

        assertCreated(response);
    }

    @Test
    public void shouldReturn201CreatedSettingLookingForHumanAsDefaultStateWhenStateIsNotSend() {
        String animal = "{\"clinicalRecord\":\"" + clinicalRecord +
                "\",\"name\":\"" + name + "\",\"registrationDate\":\"" + registrationDate +
                "\",\"animalSpecies\":\"" + animalSpecies + "\",\"estimatedAge\":\"" + estimatedAge +
                "\",\"sex\":\"" + sex + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(animal, getHttpHeaders());

        ResponseEntity<Animal> response = testClient.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, Animal.class
        );

        assertCreated(response);
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        String wrongRegistrationDate = randomAlphabetic(10);
        String animalWithWrongData = "{\"clinicalRecord\":\"" + clinicalRecord +
                "\",\"name\":\"" + name + "\",\"registrationDate\":\"" + wrongRegistrationDate +
                "\",\"animalSpecies\":\"" + animalSpecies + "\",\"estimatedAge\":\"" + estimatedAge +
                "\",\"sex\":\"12345\"}";
        HttpEntity<String> entity = new HttpEntity<>(animalWithWrongData, getHttpHeaders());

        ResponseEntity<ApiError> response = testClient.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, ApiError.class
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        String animalWithMissingData = "{\"clinicalRecord\":\"\",\"name\":\"" + name +
                "\",\"registrationDate\":\"" + registrationDate + "\",\"animalSpecies\":\"" + animalSpecies + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(animalWithMissingData, getHttpHeaders());

        ResponseEntity<ApiError> response = testClient.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, ApiError.class
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingAnAnimalThatAlreadyExists() {
        Animal animal = new Animal(
                clinicalRecord, name, registrationDate, animalSpecies, estimatedAge, sex, state
        );
        testClient.postForEntity(ANIMALS_URL, animal, Animal.class, getHttpHeaders());

        ResponseEntity<ApiError> conflictResponse = testClient.postForEntity(
                ANIMALS_URL, animal, ApiError.class, getHttpHeaders()
        );

        assertThat(conflictResponse.getStatusCode(), is(CONFLICT));
        ApiError apiError = conflictResponse.getBody();
        assertNotNull(apiError);
    }

    private void assertCreated(ResponseEntity<Animal> response) {
        assertThat(response.getStatusCode(), is(CREATED));
        Animal animal = response.getBody();
        assertNotNull(animal);
        assertNotNull(animal.getUuid());
        assertThat(animal.getClinicalRecord(), is(clinicalRecord));
        assertThat(animal.getName(), is(name));
        assertThat(animal.getRegistrationDate(), is(registrationDate));
        assertThat(animal.getAnimalSpecies(), is(animalSpecies));
        assertThat(animal.getEstimatedAge(), is(estimatedAge));
        assertThat(animal.getState(), is(state));
    }
}