package ec.animal.adoption.resources;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class AnimalResourceIntegrationTest extends IntegrationTest {

    private static final String ANIMALS_URL = "/adoption/animals";

    private String uuid;
    private String name;
    private LocalDateTime registrationDate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        uuid = randomAlphabetic(10);
        name = randomAlphabetic(10);
        registrationDate = LocalDateTime.now();
    }

    @Test
    public void shouldReturn201Created() {
        Animal animal = new Animal(uuid, name, registrationDate, Type.CAT);

        ResponseEntity<Animal> response = testRestTemplate.postForEntity(
                ANIMALS_URL, animal, Animal.class, getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is(animal));
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        String wrongRegistrationDate = randomAlphabetic(10);
        String animalForAdoptionWithWrongData = "{\"uuid\":\"" + uuid +
                "\",\"name\":\"" + name + "\",\"registrationDate\":\"" + wrongRegistrationDate + "\"}";
        HttpEntity<String> entity = new HttpEntity<String>(animalForAdoptionWithWrongData, getHttpHeaders());

        ResponseEntity<ApiError> response = testRestTemplate.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, ApiError.class
        );

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertThat(apiError.getMessage(), is("Malformed JSON request"));
        assertThat(apiError.getStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingOrInvalidDataIsProvided() {
        String animalForAdoptionWithMissingOrInvalidData = "{\"uuid\":\"\",\"registrationDate\":\"" + registrationDate + "\"}";
        HttpEntity<String> entity = new HttpEntity<String>(animalForAdoptionWithMissingOrInvalidData, getHttpHeaders());

        ResponseEntity<ApiError> response = testRestTemplate.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, ApiError.class
        );

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertThat(apiError.getMessage(), is("Validation failed"));
        assertThat(apiError.getStatus(), is(HttpStatus.BAD_REQUEST));
        assertTrue(apiError.getDebugMessage().contains("name: must not be empty"));
        assertTrue(apiError.getDebugMessage().contains("uuid: must not be empty"));
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingAnAnimalThatAlreadyExists() {
        Animal animal = new Animal(uuid, name, registrationDate, Type.DOG);
        testRestTemplate.postForEntity(ANIMALS_URL, animal, Animal.class, getHttpHeaders());

        ResponseEntity<ApiError> conflictResponse = testRestTemplate.postForEntity(
                ANIMALS_URL, animal, ApiError.class, getHttpHeaders()
        );

        assertThat(conflictResponse.getStatusCode(), is(HttpStatus.CONFLICT));
        ApiError apiError = conflictResponse.getBody();
        assertThat(apiError.getMessage(), is("The resource already exists"));
        assertThat(apiError.getStatus(), is(HttpStatus.CONFLICT));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }
}