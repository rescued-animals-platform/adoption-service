package ec.animal.adoption.resources;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CharacteristicsResourceIntegrationTest extends IntegrationTest {

    @Test
    public void shouldReturn404NotFoundWhenAnimalUuidDoesNotExist() {
        UUID randomUuid = UUID.randomUUID();

        ResponseEntity<ApiError> response = testRestTemplate.getForEntity(
                CHARACTERISTICS_URL, ApiError.class, randomUuid
        );

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn404NotFoundWhenCharacteristicsCannotBeFoundForValidAnimal() {
        ResponseEntity<Animal> animalResponse = createAndSaveAnimal();
        Animal animal = animalResponse.getBody();
        assertNotNull(animal);

        ResponseEntity<ApiError> response = testRestTemplate.getForEntity(
                CHARACTERISTICS_URL, ApiError.class, animal.getUuid()
        );

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }
}
