package ec.animal.adoption.resources;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class AnimalResourceIntegrationTest extends IntegrationTest {

    private static final String ANIMALS_URL = "/adoption/animals";

    private String clinicalRecord;
    private String name;
    private LocalDateTime registrationDate;
    private Type type;
    private EstimatedAge estimatedAge;
    private State lookingForHumanState;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        registrationDate = LocalDateTime.now();
        type = getRandomType();
        estimatedAge = getEstimatedAge();
        lookingForHumanState = new LookingForHuman(registrationDate);
    }

    @Test
    public void shouldReturn201Created() {
        Animal animalForAdoption = new Animal(
                clinicalRecord, name, registrationDate,type, estimatedAge, lookingForHumanState
        );

        ResponseEntity<Animal> response = testRestTemplate.postForEntity(
                ANIMALS_URL, animalForAdoption, Animal.class, getHttpHeaders()
        );

        assertCreated(response);
    }

    @Test
    public void shouldReturn201CreatedSettingLookingForHumanAsDefaultStateWhenStateIsNotSend() {
        String animalForAdoption = "{\"clinicalRecord\":\"" + clinicalRecord +
                "\",\"name\":\"" + name + "\",\"registrationDate\":\"" + registrationDate +
                "\",\"type\":\"" + type + "\",\"estimatedAge\":\"" + estimatedAge + "\"}";
        HttpEntity<String> entity = new HttpEntity<String>(animalForAdoption, getHttpHeaders());

        ResponseEntity<Animal> response = testRestTemplate.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, Animal.class
        );

        assertCreated(response);
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        String wrongRegistrationDate = randomAlphabetic(10);
        String animalForAdoptionWithWrongData = "{\"clinicalRecord\":\"" + clinicalRecord +
                "\",\"name\":\"" + name + "\",\"registrationDate\":\"" + wrongRegistrationDate +
                "\",\"type\":\"" + type + "\",\"estimatedAge\":\"" + estimatedAge + "\"}";
        HttpEntity<String> entity = new HttpEntity<String>(animalForAdoptionWithWrongData, getHttpHeaders());

        ResponseEntity<ApiError> response = testRestTemplate.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, ApiError.class
        );

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        String animalForAdoptionWithMissingOrInvalidData = "{\"clinicalRecord\":\"\",\"name\":\"" + name +
                "\",\"registrationDate\":\"" + registrationDate + "\",\"type\":\"" + type + "\"}";
        HttpEntity<String> entity = new HttpEntity<String>(animalForAdoptionWithMissingOrInvalidData, getHttpHeaders());

        ResponseEntity<ApiError> response = testRestTemplate.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, ApiError.class
        );

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingAnAnimalThatAlreadyExists() {
        Animal animal = new Animal(
                clinicalRecord, name, registrationDate, type, estimatedAge, lookingForHumanState
        );
        testRestTemplate.postForEntity(ANIMALS_URL, animal, Animal.class, getHttpHeaders());

        ResponseEntity<ApiError> conflictResponse = testRestTemplate.postForEntity(
                ANIMALS_URL, animal, ApiError.class, getHttpHeaders()
        );

        assertThat(conflictResponse.getStatusCode(), is(HttpStatus.CONFLICT));
        ApiError apiError = conflictResponse.getBody();
        assertNotNull(apiError);
    }

    private static Type getRandomType() {
        Random random = new Random();
        List<Type> types = Arrays.asList(Type.values());
        int randomTypeIndex = random.nextInt(types.size());
        return types.get(randomTypeIndex);
    }

    private static EstimatedAge getEstimatedAge() {
        Random random = new Random();
        List<EstimatedAge> estimatedAges = Arrays.asList(EstimatedAge.values());
        int randomEstimatedAgeIndex = random.nextInt(estimatedAges.size());
        return estimatedAges.get(randomEstimatedAgeIndex);
    }

    private void assertCreated(ResponseEntity<Animal> response) {
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        Animal animal = response.getBody();
        assertNotNull(animal);
        assertNotNull(animal.getUuid());
        assertThat(animal.getClinicalRecord(), is(clinicalRecord));
        assertThat(animal.getName(), is(name));
        assertThat(animal.getRegistrationDate(), is(registrationDate));
        assertThat(animal.getType(), is(type));
        assertThat(animal.getEstimatedAge(), is(estimatedAge));
        assertThat(animal.getState(), is(lookingForHumanState));
    }
}