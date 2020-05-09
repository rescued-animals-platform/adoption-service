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

import ec.animal.adoption.api.model.animal.CreateAnimalResponse;
import ec.animal.adoption.api.model.error.ApiErrorResponse;
import ec.animal.adoption.domain.story.Story;
import ec.animal.adoption.domain.story.StoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CONFLICT;

public class StoryResourceApiTest extends AbstractApiTest {

    private UUID animalId;

    @BeforeEach
    public void setUp() {
        CreateAnimalResponse createAnimalResponse = createRandomAnimalWithDefaultLookingForHumanState();
        animalId = createAnimalResponse.getAnimalId();
    }

    @Test
    public void shouldReturn201CreatedWithStory() {
        Story story = StoryBuilder.random().build();

        webTestClient.post()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(story)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody(Story.class)
                     .consumeWith(storyEntityExchangeResult -> {
                         Story createdStory = storyEntityExchangeResult.getResponseBody();
                         assertEquals(story, createdStory);
                     });
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAStoryAndJsonCannotBeParsed() {
        String storyWithWrongData = "{\"another\":\"" + randomAlphabetic(10) + "\"}";

        webTestClient.post()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(storyWithWrongData)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiErrorResponse.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAStoryAndMissingDataIsProvided() {
        String storyWithMissingData = "{\"text\":\"\"}";

        webTestClient.post()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(storyWithMissingData)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiErrorResponse.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingStoryForNonExistentAnimal() {
        webTestClient.post()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(StoryBuilder.random().build())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiErrorResponse.class);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingStoryThatAlreadyExist() {
        Story story = StoryBuilder.random().build();
        webTestClient.post()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(story)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        webTestClient.post()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(story)
                     .exchange()
                     .expectStatus()
                     .isEqualTo(CONFLICT)
                     .expectBody(ApiErrorResponse.class);
    }

    @Test
    public void shouldReturn200OkWithUpdatedStory() {
        Story createdStory = createStoryForAnimal(animalId, StoryBuilder.random().build());
        String newStoryText = randomAlphabetic(10);
        Story newStory = StoryBuilder.random().withText(newStoryText).build();

        webTestClient.put()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(newStory)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Story.class)
                     .consumeWith(storyEntityExchangeResult -> {
                         Story updatedStory = storyEntityExchangeResult.getResponseBody();
                         assertNotEquals(createdStory, updatedStory);
                         assertEquals(newStory, updatedStory);
                     });
    }

    @Test
    public void shouldReturn200OkWhenUpdatingStoryBeforeCreatingOne() {
        Story story = StoryBuilder.random().build();

        webTestClient.put()
                     .uri(STORY_ADMIN_URL, animalId)
                     .bodyValue(story)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Story.class)
                     .consumeWith(storyEntityExchangeResult -> {
                         Story updatedStory = storyEntityExchangeResult.getResponseBody();
                         assertEquals(story, updatedStory);
                     });
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAStoryAndJsonCannotBeParsed() {
        String storyWithWrongData = "{\"another\":\"" + randomAlphabetic(10) + "\"}";

        webTestClient.put()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(storyWithWrongData)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiErrorResponse.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAStoryAndMissingDataIsProvided() {
        String storyWithMissingData = "{\"text\":\"\"}";

        webTestClient.put()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(storyWithMissingData)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiErrorResponse.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenUpdatingStoryForNonExistentAnimal() {
        webTestClient.put()
                     .uri(STORY_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(StoryBuilder.random().build())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiErrorResponse.class);
    }

    @Test
    public void shouldReturn200OkWithStory() {
        Story createdStory = webTestClient.post()
                                          .uri(STORY_ADMIN_URL, animalId)
                                          .bodyValue(StoryBuilder.random().build())
                                          .exchange()
                                          .expectStatus()
                                          .isCreated()
                                          .expectBody(Story.class)
                                          .returnResult()
                                          .getResponseBody();

        assertNotNull(createdStory);

        webTestClient.get()
                     .uri(GET_STORY_URL, animalId)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Story.class)
                     .isEqualTo(createdStory);
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalIdDoesNotExist() {
        webTestClient.get()
                     .uri(GET_STORY_URL, UUID.randomUUID())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiErrorResponse.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenStoryCannotBeFoundForValidAnimal() {
        webTestClient.get()
                     .uri(GET_STORY_URL, animalId)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiErrorResponse.class);
    }

    private static Story createStoryForAnimal(final UUID animalId, final Story story) {
        return webTestClient.post()
                            .uri(STORY_ADMIN_URL, animalId)
                            .bodyValue(story)
                            .exchange()
                            .expectStatus()
                            .isCreated()
                            .expectBody(Story.class)
                            .returnResult()
                            .getResponseBody();
    }
}
