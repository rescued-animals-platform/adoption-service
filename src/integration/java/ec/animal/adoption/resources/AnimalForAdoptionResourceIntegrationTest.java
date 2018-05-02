package ec.animal.adoption.resources;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.AnimalForAdoption;
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

public class AnimalForAdoptionResourceIntegrationTest extends IntegrationTest {

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
        AnimalForAdoption animalForAdoption = new AnimalForAdoption(uuid, name, registrationDate);

        ResponseEntity<AnimalForAdoption> response = testRestTemplate.postForEntity(
                ANIMALS_URL, animalForAdoption, AnimalForAdoption.class, getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is(animalForAdoption));
    }

    @Test
    public void shouldReturn400BadRequest() {
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
    public void shouldReturn409ConflictWhenAnimalAlreadyExists() {
        AnimalForAdoption animalForAdoption = new AnimalForAdoption(uuid, name, registrationDate);
        testRestTemplate.postForEntity(ANIMALS_URL, animalForAdoption, AnimalForAdoption.class, getHttpHeaders());

        ResponseEntity<ApiError> conflictResponse = testRestTemplate.postForEntity(
                ANIMALS_URL, animalForAdoption, ApiError.class, getHttpHeaders()
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