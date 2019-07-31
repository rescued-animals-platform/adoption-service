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

import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.error.ApiError;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.springframework.http.HttpStatus.CONFLICT;

@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
public class CharacteristicsResourceIntegrationTest extends ResourceIntegrationTest {

    @Test
    public void shouldReturn201CreatedWithCharacteristics() {
        Animal animal = createAndSaveAnimal();
        Characteristics characteristics = CharacteristicsBuilder.random().build();

        webTestClient.post()
                .uri(CHARACTERISTICS_URL, animal.getUuid())
                .syncBody(characteristics)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Characteristics.class)
                .isEqualTo(characteristics);
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        String characteristicsWithWrongData = "{\"size\":\"" + Size.SMALL +
                "\",\"physicalActivity\":\"" + PhysicalActivity.LOW +
                "\",\"temperaments\":{\"sociability\":\"" + randomAlphabetic(10) +
                "\"},\"friendlyWith\":[\"" + FriendlyWith.CATS + "\"]}";

        webTestClient.post()
                .uri(CHARACTERISTICS_URL, UUID.randomUUID())
                .syncBody(characteristicsWithWrongData)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        String characteristicsWithMissingData = "{\"size\":\"" + Size.SMALL +
                "\",\"temperaments\":{\"balance\":\"" + Balance.BALANCED +
                "\"},\"friendlyWith\":[\"" + FriendlyWith.CATS + "\"]}";

        webTestClient.post()
                .uri(CHARACTERISTICS_URL, UUID.randomUUID())
                .syncBody(characteristicsWithMissingData)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingCharacteristicsForNonExistentAnimal() {
        Characteristics characteristics = CharacteristicsBuilder.random().build();

        webTestClient.post()
                .uri(CHARACTERISTICS_URL, UUID.randomUUID())
                .syncBody(characteristics)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingCharacteristicsThatAlreadyExist() {
        Animal animal = createAndSaveAnimal();
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        webTestClient.post()
                .uri(CHARACTERISTICS_URL, animal.getUuid())
                .syncBody(characteristics)
                .exchange()
                .expectStatus()
                .isCreated();

        webTestClient.post()
                .uri(CHARACTERISTICS_URL, animal.getUuid())
                .syncBody(characteristics)
                .exchange()
                .expectStatus()
                .isEqualTo(CONFLICT)
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithCharacteristics() {
        Animal animal = createAndSaveAnimal();
        Characteristics createdCharacteristics = webTestClient.post()
                .uri(CHARACTERISTICS_URL, animal.getUuid())
                .syncBody(CharacteristicsBuilder.random().build())
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Characteristics.class)
                .returnResult()
                .getResponseBody();

        webTestClient.get()
                .uri(CHARACTERISTICS_URL, animal.getUuid())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Characteristics.class)
                .isEqualTo(createdCharacteristics);
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalUuidDoesNotExist() {
        webTestClient.get()
                .uri(CHARACTERISTICS_URL, UUID.randomUUID())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenCharacteristicsCannotBeFoundForValidAnimal() {
        Animal animal = createAndSaveAnimal();

        webTestClient.get()
                .uri(CHARACTERISTICS_URL, animal.getUuid())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }
}
