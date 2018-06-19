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

package ec.animal.adoption.resources;

import ec.animal.adoption.AbstractIntegrationTest;
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

public class CharacteristicsResourceIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void shouldReturn201CreatedWithCharacteristics() {
        Animal animal = createAndSaveAnimal();
        Characteristics characteristics = new Characteristics(
                Size.TINY,
                PhysicalActivity.LOW,
                new Temperaments(Sociability.SHY, null, Balance.POSSESSIVE),
                FriendlyWith.ADULTS
        );

        ResponseEntity<Characteristics> response = testClient.postForEntity(
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

        ResponseEntity<ApiError> response = testClient.exchange(
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

        ResponseEntity<ApiError> response = testClient.exchange(
                CHARACTERISTICS_URL, HttpMethod.POST, entity, ApiError.class, UUID.randomUUID()
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingCharacteristicsForNonExistentAnimal() {
        Characteristics characteristics = new Characteristics(
                Size.TINY,
                PhysicalActivity.LOW,
                new Temperaments(Sociability.VERY_SOCIABLE, Docility.NEITHER_DOCILE_NOR_DOMINANT, Balance.BALANCED),
                FriendlyWith.ADULTS
        );

        ResponseEntity<ApiError> response = testClient.postForEntity(
                CHARACTERISTICS_URL, characteristics, ApiError.class, UUID.randomUUID(), getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingCharacteristicsThatAlreadyExist() {
        Animal animal = createAndSaveAnimal();
        Characteristics characteristics = new Characteristics(
                Size.TINY,
                PhysicalActivity.LOW,
                new Temperaments(Sociability.VERY_SOCIABLE, Docility.NEITHER_DOCILE_NOR_DOMINANT, Balance.BALANCED),
                FriendlyWith.ADULTS
        );
        testClient.postForEntity(
                CHARACTERISTICS_URL, characteristics, Characteristics.class, animal.getUuid(), getHttpHeaders()
        );

        ResponseEntity<ApiError> conflictResponse = testClient.postForEntity(
                CHARACTERISTICS_URL, characteristics, ApiError.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(conflictResponse.getStatusCode(), is(CONFLICT));
        ApiError apiError = conflictResponse.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn200OkWithCharacteristics() {
        Animal animal = createAndSaveAnimal();
        ResponseEntity<Characteristics> createdCharacteristicsResponse = testClient.postForEntity(
                CHARACTERISTICS_URL, new Characteristics(
                        Size.TINY,
                        PhysicalActivity.LOW,
                        new Temperaments(Sociability.SHY, Docility.DOCILE, Balance.BALANCED),
                        FriendlyWith.CATS
                ), Characteristics.class, animal.getUuid(), getHttpHeaders()
        );
        Characteristics createdCharacteristics = createdCharacteristicsResponse.getBody();

        ResponseEntity<Characteristics> response = testClient.getForEntity(
                CHARACTERISTICS_URL, Characteristics.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(OK));
        Characteristics foundCharacteristics = response.getBody();
        assertThat(foundCharacteristics, is(createdCharacteristics));
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalUuidDoesNotExist() {
        UUID randomUuid = UUID.randomUUID();

        ResponseEntity<ApiError> response = testClient.getForEntity(
                CHARACTERISTICS_URL, ApiError.class, randomUuid
        );

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn404NotFoundWhenCharacteristicsCannotBeFoundForValidAnimal() {
        Animal animal = createAndSaveAnimal();

        ResponseEntity<ApiError> response = testClient.getForEntity(
                CHARACTERISTICS_URL, ApiError.class, animal.getUuid()
        );

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }
}
