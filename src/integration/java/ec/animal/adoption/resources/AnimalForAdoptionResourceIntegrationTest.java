package ec.animal.adoption.resources;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.AnimalForAdoption;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AnimalForAdoptionResourceIntegrationTest extends IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturn201Created() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String uuid = randomAlphabetic(10);
        String name = "Pocho";
        LocalDateTime registrationDate = LocalDateTime.now();
        AnimalForAdoption animalForAdoption = new AnimalForAdoption(uuid, name, registrationDate);

        ResponseEntity<AnimalForAdoption> response = restTemplate.postForEntity(
                "/adoption/animal", animalForAdoption, AnimalForAdoption.class, headers
        );

        assertThat(response.getStatusCode().value(), is(HttpStatus.CREATED.value()));
        assertThat(response.getBody(), is(animalForAdoption));
    }
}