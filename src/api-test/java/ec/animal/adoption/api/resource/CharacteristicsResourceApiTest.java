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

package ec.animal.adoption.api.resource;

import ec.animal.adoption.api.model.animal.AnimalCreateUpdateResponse;
import ec.animal.adoption.api.model.characteristics.CharacteristicsRequest;
import ec.animal.adoption.api.model.characteristics.CharacteristicsRequestBuilder;
import ec.animal.adoption.api.model.characteristics.CharacteristicsResponse;
import ec.animal.adoption.api.model.characteristics.temperaments.TemperamentsRequest;
import ec.animal.adoption.api.model.characteristics.temperaments.TemperamentsRequestBuilder;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.*;

public class CharacteristicsResourceApiTest extends AbstractApiTest {

    private UUID animalId;
    private Size size;
    private PhysicalActivity physicalActivity;
    private Sociability sociability;
    private Docility docility;
    private Balance balance;
    private FriendlyWith friendlyWith;
    private TemperamentsRequest temperamentsRequest;

    @BeforeEach
    public void setUp() {
        size = getRandomSize();
        physicalActivity = getRandomPhysicalActivity();
        sociability = getRandomSociability();
        docility = getRandomDocility();
        balance = getRandomBalance();
        friendlyWith = getRandomFriendlyWith();
        temperamentsRequest = TemperamentsRequestBuilder.empty()
                                                        .withSociability(sociability)
                                                        .withBalance(balance)
                                                        .withDocility(docility)
                                                        .build();
        AnimalCreateUpdateResponse animalCreateUpdateResponse = createRandomAnimalWithDefaultLookingForHumanState();
        animalId = animalCreateUpdateResponse.getAnimalId();
    }

    @Test
    public void shouldReturn201CreatedWithCharacteristics() {
        CharacteristicsRequest characteristicsRequest = CharacteristicsRequestBuilder
                .random()
                .withSize(size)
                .withPhysicalActivity(physicalActivity)
                .withTemperaments(temperamentsRequest)
                .withFriendlyWith(friendlyWith)
                .build();

        webTestClient.post()
                     .uri(CHARACTERISTICS_ADMIN_URL, animalId)
                     .bodyValue(characteristicsRequest)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .jsonPath("$.size").isEqualTo(size.name())
                     .jsonPath("$.physicalActivity").isEqualTo(physicalActivity.name())
                     .jsonPath("$.temperaments.sociability").isEqualTo(sociability.name())
                     .jsonPath("$.temperaments.docility").isEqualTo(docility.name())
                     .jsonPath("$.temperaments.balance").isEqualTo(balance.name())
                     .jsonPath("$.friendlyWith[0]").isEqualTo(friendlyWith.name());
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingCharacteristicsAndJsonCannotBeParsed() throws JSONException {
        String invalidSociability = randomAlphabetic(10);
        String characteristicsRequestWithWrongData = new JSONObject()
                .put("size", Size.SMALL)
                .put("physicalActivity", PhysicalActivity.LOW)
                .put("temperaments", new JSONObject().put("sociability", invalidSociability))
                .put("friendlyWith", new JSONArray().put(FriendlyWith.CATS))
                .toString();

        webTestClient.post()
                     .uri(CHARACTERISTICS_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(characteristicsRequestWithWrongData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Malformed JSON request")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingCharacteristicsAndMissingDataIsProvided() throws JSONException {
        String characteristicsRequestWithMissingData = new JSONObject()
                .put("size", Size.SMALL)
                .put("temperaments", new JSONObject().put("sociability", Sociability.SHY))
                .put("friendlyWith", new JSONArray().put(FriendlyWith.CATS))
                .toString();

        webTestClient.post()
                     .uri(CHARACTERISTICS_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(characteristicsRequestWithMissingData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Validation failed")
                     .jsonPath("$.subErrors").isNotEmpty();
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingCharacteristicsForNonExistentAnimal() {
        CharacteristicsRequest characteristicsRequest = CharacteristicsRequestBuilder.random().build();

        webTestClient.post()
                     .uri(CHARACTERISTICS_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(characteristicsRequest)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingCharacteristicsThatAlreadyExist() {
        CharacteristicsRequest characteristicsRequest = CharacteristicsRequestBuilder.random().build();
        createCharacteristicsForAnimal(animalId, characteristicsRequest);

        webTestClient.post()
                     .uri(CHARACTERISTICS_ADMIN_URL, animalId)
                     .bodyValue(characteristicsRequest)
                     .exchange()
                     .expectStatus()
                     .isEqualTo(CONFLICT)
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(CONFLICT.name())
                     .jsonPath("$.message").isEqualTo("The resource already exists")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn200OkWithUpdatedCharacteristics() {
        createCharacteristicsForAnimal(animalId, CharacteristicsRequestBuilder.random().build());
        CharacteristicsRequest updateCharacteristicsRequest = CharacteristicsRequestBuilder
                .random()
                .withSize(size)
                .withPhysicalActivity(physicalActivity)
                .withTemperaments(temperamentsRequest)
                .withFriendlyWith(friendlyWith)
                .build();

        webTestClient.put()
                     .uri(CHARACTERISTICS_ADMIN_URL, animalId)
                     .bodyValue(updateCharacteristicsRequest)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.size").isEqualTo(size.name())
                     .jsonPath("$.physicalActivity").isEqualTo(physicalActivity.name())
                     .jsonPath("$.temperaments.sociability").isEqualTo(sociability.name())
                     .jsonPath("$.temperaments.docility").isEqualTo(docility.name())
                     .jsonPath("$.temperaments.balance").isEqualTo(balance.name())
                     .jsonPath("$.friendlyWith[0]").isEqualTo(friendlyWith.name());
    }

    @Test
    public void shouldReturn200OkWhenUpdatingCharacteristicsBeforeCreatingThem() {
        CharacteristicsRequest updateCharacteristicsRequest = CharacteristicsRequestBuilder
                .random()
                .withSize(size)
                .withPhysicalActivity(physicalActivity)
                .withTemperaments(temperamentsRequest)
                .withFriendlyWith(friendlyWith)
                .build();

        webTestClient.put()
                     .uri(CHARACTERISTICS_ADMIN_URL, animalId)
                     .bodyValue(updateCharacteristicsRequest)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.size").isEqualTo(size.name())
                     .jsonPath("$.physicalActivity").isEqualTo(physicalActivity.name())
                     .jsonPath("$.temperaments.sociability").isEqualTo(sociability.name())
                     .jsonPath("$.temperaments.docility").isEqualTo(docility.name())
                     .jsonPath("$.temperaments.balance").isEqualTo(balance.name())
                     .jsonPath("$.friendlyWith[0]").isEqualTo(friendlyWith.name());
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingCharacteristicsAndJsonCannotBeParsed() throws JSONException {
        String invalidSize = randomAlphabetic(10);
        String updateCharacteristicsRequestWithWrongData = new JSONObject()
                .put("size", invalidSize)
                .put("physicalActivity", PhysicalActivity.LOW)
                .put("temperaments", new JSONObject().put("sociability", sociability))
                .put("friendlyWith", new JSONArray().put(FriendlyWith.CATS))
                .toString();

        webTestClient.put()
                     .uri(CHARACTERISTICS_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(updateCharacteristicsRequestWithWrongData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Malformed JSON request")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingCharacteristicsAndMissingDataIsProvided() throws JSONException {
        String updateCharacteristicsRequestWithMissingData = new JSONObject()
                .put("size", Size.SMALL)
                .put("physicalActivity", PhysicalActivity.LOW)
                .put("friendlyWith", new JSONArray().put(FriendlyWith.CATS))
                .toString();

        webTestClient.put()
                     .uri(CHARACTERISTICS_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(updateCharacteristicsRequestWithMissingData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Validation failed")
                     .jsonPath("$.subErrors").isNotEmpty();
    }

    @Test
    public void shouldReturn404NotFoundWhenUpdatingCharacteristicsForNonExistentAnimal() {
        CharacteristicsRequest updateCharacteristicsRequest = CharacteristicsRequestBuilder.random().build();

        webTestClient.put()
                     .uri(CHARACTERISTICS_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(updateCharacteristicsRequest)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn200OkWithCharacteristics() {
        CharacteristicsResponse createdCharacteristicsResponse = createCharacteristicsForAnimal(
                animalId, CharacteristicsRequestBuilder.random().build()
        );

        webTestClient.get()
                     .uri(CHARACTERISTICS_URL, animalId)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(CharacteristicsResponse.class)
                     .consumeWith(entity -> {
                         CharacteristicsResponse foundCharacteristicsResponse = entity.getResponseBody();
                         Assertions.assertThat(foundCharacteristicsResponse)
                                   .usingRecursiveComparison()
                                   .isEqualTo(createdCharacteristicsResponse);
                     });
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalIdDoesNotExist() {
        webTestClient.get()
                     .uri(CHARACTERISTICS_URL, UUID.randomUUID())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn404NotFoundWhenCharacteristicsCannotBeFoundForValidAnimal() {
        webTestClient.get()
                     .uri(CHARACTERISTICS_URL, animalId)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    private static CharacteristicsResponse createCharacteristicsForAnimal(
            final UUID animalId, final CharacteristicsRequest characteristicsRequest
    ) {
        CharacteristicsResponse createdCharacteristicsResponse = webTestClient
                .post()
                .uri(CHARACTERISTICS_ADMIN_URL, animalId)
                .bodyValue(characteristicsRequest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(CharacteristicsResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(createdCharacteristicsResponse);

        return createdCharacteristicsResponse;
    }
}
