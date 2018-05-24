package ec.animal.adoption.resources;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperament.Balance;
import ec.animal.adoption.domain.characteristics.temperament.Sociability;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

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

    @Test
    public void shouldReturn201CreatedWithCharacteristics() {
        ResponseEntity<Animal> animalResponse = createAndSaveAnimal();
        Animal animal = animalResponse.getBody();
        assertNotNull(animal);
        Characteristics characteristics = new Characteristics(
                Size.TINY, PhysicalActivity.LOW, Arrays.asList(Sociability.SHY, Balance.POSSESSIVE), FriendlyWith.ADULTS
        );

        ResponseEntity<Characteristics> response = testRestTemplate.postForEntity(
                CHARACTERISTICS_URL, characteristics, Characteristics.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(CREATED));
        Characteristics createdCharacteristics = response.getBody();
        assertReflectionEquals(characteristics, createdCharacteristics);
    }
}
