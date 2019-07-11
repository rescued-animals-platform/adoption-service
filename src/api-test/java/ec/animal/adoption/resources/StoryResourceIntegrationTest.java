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

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Story;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class StoryResourceIntegrationTest extends AbstractResourceIntegrationTest {

    @Test
    public void shouldReturn201CreatedWithStory() {
        Animal animal = createAndSaveAnimal();
        Story story = new Story(randomAlphabetic(300));

        this.webClient.post()
                .uri(STORY_URL, animal.getUuid())
                .syncBody(story)
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

        this.webClient.post()
                .uri(STORY_URL, UUID.randomUUID())
                .syncBody(storyWithWrongData)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        String storyWithMissingData = "{\"text\":\"\"}";

        this.webClient.post()
                .uri(STORY_URL, UUID.randomUUID())
                .syncBody(storyWithMissingData)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingStoryForNonExistentAnimal() {
        this.webClient.post()
                .uri(STORY_URL, UUID.randomUUID())
                .syncBody(new Story(randomAlphabetic(300)))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingStoryThatAlreadyExist() {
        Animal animal = createAndSaveAnimal();
        Story story = new Story(randomAlphabetic(300));
        this.webClient.post()
                .uri(STORY_URL, animal.getUuid())
                .syncBody(story)
                .exchange()
                .expectStatus()
                .isCreated();

        this.webClient.post()
                .uri(STORY_URL, animal.getUuid())
                .syncBody(story)
                .exchange()
                .expectStatus()
                .isEqualTo(CONFLICT)
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithStory() {
        Animal animal = createAndSaveAnimal();
        Story createdStory = this.webClient.post()
                .uri(STORY_URL, animal.getUuid())
                .syncBody(new Story(randomAlphabetic(100)))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Story.class)
                .returnResult()
                .getResponseBody();

        this.webClient.get()
                .uri(STORY_URL, animal.getUuid())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Story.class)
                .isEqualTo(createdStory);
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalUuidDoesNotExist() {
        this.webClient.get()
                .uri(STORY_URL, UUID.randomUUID())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenStoryCannotBeFoundForValidAnimal() {
        Animal animal = createAndSaveAnimal();

        this.webClient.get()
                .uri(STORY_URL, animal.getUuid())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }
}
