package ec.animal.adoption.resources;

import ec.animal.adoption.IntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class CharacteristicsResourceIntegrationTest extends IntegrationTest {

    @Test
    public void shouldReturn201CreatedWithCharacteristics() {
        ResponseEntity<Animal> animalResponse = createAndSaveAnimal();
        Animal animal = animalResponse.getBody();
        assertNotNull(animal);
        Characteristics characteristics = new Characteristics(
                Size.TINY,
                PhysicalActivity.LOW,
                new Temperaments(Sociability.SHY, Docility.DOCILE, Balance.POSSESSIVE),
                FriendlyWith.ADULTS
        );

        ResponseEntity<Characteristics> response = testRestTemplate.postForEntity(
                CHARACTERISTICS_URL, characteristics, Characteristics.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(CREATED));
        Characteristics createdCharacteristics = response.getBody();
        assertReflectionEquals(characteristics, createdCharacteristics);
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        String characteristicsWithWrongData = "{\"size\":\"" + Size.SMALL +
                "\",\"physicalActivity\":\"" + PhysicalActivity.LOW +
                "\",\"temperaments\":{\"sociability\":\"" + randomAlphabetic(10) +
                "\"},\"friendlyWith\":[\"" + FriendlyWith.CATS + "\"]}";
        HttpEntity<String> entity = new HttpEntity<>(characteristicsWithWrongData, getHttpHeaders());

        ResponseEntity<ApiError> response = testRestTemplate.exchange(
                CHARACTERISTICS_URL, HttpMethod.POST, entity, ApiError.class, UUID.randomUUID()
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        String characteristicsWithMissingData = "{\"size\":\"" + Size.SMALL +
                "\",\"temperaments\":{\"balance\":\"" + Balance.BALANCED +
                "\"},\"friendlyWith\":[\"" + FriendlyWith.CATS + "\"]}";
        HttpEntity<String> entity = new HttpEntity<>(characteristicsWithMissingData, getHttpHeaders());

        ResponseEntity<ApiError> response = testRestTemplate.exchange(
                CHARACTERISTICS_URL, HttpMethod.POST, entity, ApiError.class, UUID.randomUUID()
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingCharacteristicsThatAlreadyExist() {
        ResponseEntity<Animal> animalResponse = createAndSaveAnimal();
        Animal animal = animalResponse.getBody();
        assertNotNull(animal);
        Characteristics characteristics = new Characteristics(
                Size.TINY,
                PhysicalActivity.LOW,
                new Temperaments(Sociability.VERY_SOCIABLE, Docility.NEITHER_DOCILE_NOR_DOMINANT, Balance.BALANCED),
                FriendlyWith.ADULTS
        );
        testRestTemplate.postForEntity(
                CHARACTERISTICS_URL, characteristics, Characteristics.class, animal.getUuid(), getHttpHeaders()
        );

        ResponseEntity<ApiError> conflictResponse = testRestTemplate.postForEntity(
                CHARACTERISTICS_URL, characteristics, ApiError.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(conflictResponse.getStatusCode(), is(CONFLICT));
        ApiError apiError = conflictResponse.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn200OkWithCharacteristics() {
        ResponseEntity<Animal> animalResponse = createAndSaveAnimal();
        Animal animal = animalResponse.getBody();
        assertNotNull(animal);
        ResponseEntity<Characteristics> createdCharacteristicsResponse = testRestTemplate.postForEntity(
                CHARACTERISTICS_URL, new Characteristics(
                        Size.TINY,
                        PhysicalActivity.LOW,
                        new Temperaments(Sociability.SHY, Docility.DOCILE, Balance.BALANCED),
                        FriendlyWith.CATS
                ), Characteristics.class, animal.getUuid(), getHttpHeaders()
        );
        Characteristics createdCharacteristics = createdCharacteristicsResponse.getBody();

        ResponseEntity<Characteristics> response = testRestTemplate.getForEntity(
                CHARACTERISTICS_URL, Characteristics.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(OK));
        Characteristics foundCharacteristics = response.getBody();
        assertThat(foundCharacteristics, is(createdCharacteristics));
    }

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
