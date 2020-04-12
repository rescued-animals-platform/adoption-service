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

package ec.animal.adoption.resource;

import ec.animal.adoption.builders.StoryBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Story;
import ec.animal.adoption.domain.error.ApiError;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
public class StoryResourceApiTest extends AbstractApiTest {

    @Test
    public void shouldReturn201CreatedWithStory() {
        Animal animal = createRandomAnimalWithDefaultLookingForHumanState();
        Story story = StoryBuilder.random().build();

        webTestClient.post()
                     .uri(STORY_URL, animal.getUuid())
                     .bodyValue(story)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody(Story.class)
                     .consumeWith(storyEntityExchangeResult -> {
                         Story createdStory = storyEntityExchangeResult.getResponseBody();
                         assertReflectionEquals(story, createdStory);
                     });
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        String storyWithWrongData = "{\"another\":\"" + randomAlphabetic(10) + "\"}";

        webTestClient.post()
                     .uri(STORY_URL, UUID.randomUUID())
                     .bodyValue(storyWithWrongData)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        String storyWithMissingData = "{\"text\":\"\"}";

        webTestClient.post()
                     .uri(STORY_URL, UUID.randomUUID())
                     .bodyValue(storyWithMissingData)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingStoryForNonExistentAnimal() {
        webTestClient.post()
                     .uri(STORY_URL, UUID.randomUUID())
                     .bodyValue(StoryBuilder.random().build())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingStoryThatAlreadyExist() {
        Animal animal = createRandomAnimalWithDefaultLookingForHumanState();
        Story story = StoryBuilder.random().build();
        webTestClient.post()
                     .uri(STORY_URL, animal.getUuid())
                     .bodyValue(story)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        webTestClient.post()
                     .uri(STORY_URL, animal.getUuid())
                     .bodyValue(story)
                     .exchange()
                     .expectStatus()
                     .isEqualTo(CONFLICT)
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithStory() {
        Animal animal = createRandomAnimalWithDefaultLookingForHumanState();
        Story createdStory = webTestClient.post()
                                          .uri(STORY_URL, animal.getUuid())
                                          .bodyValue(StoryBuilder.random().build())
                                          .exchange()
                                          .expectStatus()
                                          .isCreated()
                                          .expectBody(Story.class)
                                          .returnResult()
                                          .getResponseBody();

        assertNotNull(createdStory);

        webTestClient.get()
                     .uri(STORY_URL, animal.getUuid())
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Story.class)
                     .isEqualTo(createdStory);
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalUuidDoesNotExist() {
        webTestClient.get()
                     .uri(STORY_URL, UUID.randomUUID())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenStoryCannotBeFoundForValidAnimal() {
        Animal animal = createRandomAnimalWithDefaultLookingForHumanState();

        webTestClient.get()
                     .uri(STORY_URL, animal.getUuid())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiError.class);
    }
}
