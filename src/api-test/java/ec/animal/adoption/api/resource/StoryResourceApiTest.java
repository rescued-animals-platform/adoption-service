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
import ec.animal.adoption.api.model.story.StoryRequest;
import ec.animal.adoption.api.model.story.StoryResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class StoryResourceApiTest extends AbstractApiTest {

    private UUID animalId;

    @BeforeEach
    public void setUp() {
        AnimalCreateUpdateResponse animalCreateUpdateResponse = createRandomAnimalWithDefaultLookingForHumanState();
        animalId = animalCreateUpdateResponse.getAnimalId();
    }

    @Test
    public void shouldReturn201CreatedWithStory() {
        String text = randomAlphabetic(10);
        StoryRequest storyRequest = new StoryRequest(text);

        webTestClient.post()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(storyRequest)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .jsonPath("$.text").isEqualTo(text);
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAStoryAndJsonCannotBeParsed() {
        String storyWithWrongData = "{\"text\":" + randomAlphabetic(10) + "}";

        webTestClient.post()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(storyWithWrongData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Malformed JSON request")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAStoryAndMissingDataIsProvided() {
        String storyWithMissingData = "{\"text\":\"\"}";

        webTestClient.post()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(storyWithMissingData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Validation failed")
                     .jsonPath("$.subErrors").isNotEmpty();
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingStoryForNonExistentAnimal() {
        webTestClient.post()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(new StoryRequest(randomAlphabetic(10)))
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingStoryThatAlreadyExist() {
        StoryRequest storyRequest = new StoryRequest(randomAlphabetic(10));
        webTestClient.post()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(storyRequest)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        webTestClient.post()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(storyRequest)
                     .exchange()
                     .expectStatus()
                     .isEqualTo(CONFLICT)
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(CONFLICT.name())
                     .jsonPath("$.message").isEqualTo("The resource already exists")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn200OkWithUpdatedStory() {
        createStoryForAnimal(animalId, new StoryRequest(randomAlphabetic(10)));
        String newStoryText = randomAlphabetic(10);
        StoryRequest updateStoryRequest = new StoryRequest(newStoryText);

        webTestClient.put()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(updateStoryRequest)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.text").isEqualTo(newStoryText);
    }

    @Test
    public void shouldReturn200OkWhenUpdatingStoryBeforeCreatingOne() {
        String text = randomAlphabetic(10);
        StoryRequest storyRequest = new StoryRequest(text);

        webTestClient.put()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(storyRequest)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.text").isEqualTo(text);
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAStoryAndJsonCannotBeParsed() {
        String storyWithWrongData = "{\"text\":" + randomAlphabetic(10) + "}";

        webTestClient.put()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(storyWithWrongData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Malformed JSON request")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAStoryAndMissingDataIsProvided() {
        String storyWithMissingData = "{\"text\":\"\"}";

        webTestClient.put()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(storyWithMissingData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Validation failed")
                     .jsonPath("$.subErrors").isNotEmpty();
    }

    @Test
    public void shouldReturn404NotFoundWhenUpdatingStoryForNonExistentAnimal() {
        webTestClient.put()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(new StoryRequest(randomAlphabetic(10)))
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn200OkWithStory() {
        StoryResponse createdStoryResponse = createStoryForAnimal(animalId, new StoryRequest(randomAlphabetic(10)));

        webTestClient.get()
                     .uri(STORY_URL, animalId)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(StoryResponse.class)
                     .consumeWith(entity -> {
                         StoryResponse foundStoryResponse = entity.getResponseBody();

                         Assertions.assertThat(foundStoryResponse)
                                   .usingRecursiveComparison()
                                   .isEqualTo(createdStoryResponse);
                     });
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalIdDoesNotExist() {
        webTestClient.get()
                     .uri(STORY_URL, UUID.randomUUID())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn404NotFoundWhenStoryCannotBeFoundForValidAnimal() {
        webTestClient.get()
                     .uri(STORY_URL, animalId)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    private static StoryResponse createStoryForAnimal(final UUID animalId, final StoryRequest storyRequest) {
        StoryResponse storyResponse = webTestClient.post()
                                                   .uri(STORY_ADMIN_URL, animalId)
                                                   .bodyValue(storyRequest)
                                                   .exchange()
                                                   .expectStatus()
                                                   .isCreated()
                                                   .expectBody(StoryResponse.class)
                                                   .returnResult()
                                                   .getResponseBody();
        assertNotNull(storyResponse);

        return storyResponse;
    }
}
