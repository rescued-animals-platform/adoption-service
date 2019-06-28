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

public class StoryResourceResourceIntegrationTest extends AbstractResourceIntegrationTest {

    @Test
    public void shouldReturn201CreatedWithStory() {
        Animal animal = createAndSaveAnimal();
        Story story = new Story(randomAlphabetic(300));

        ResponseEntity<Story> response = testClient.postForEntity(
                STORY_URL, story, Story.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(CREATED));
        Story createdStory = response.getBody();
        assertReflectionEquals(story, createdStory);
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        String storyWithWrongData = "{\"another\":\"" + randomAlphabetic(10) + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(storyWithWrongData, getHttpHeaders());

        ResponseEntity<ApiError> response = testClient.exchange(
                STORY_URL, HttpMethod.POST, entity, ApiError.class, UUID.randomUUID()
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        String storyWithMissingData = "{\"text\":\"\"}";
        HttpEntity<String> entity = new HttpEntity<>(storyWithMissingData, getHttpHeaders());

        ResponseEntity<ApiError> response = testClient.exchange(
                STORY_URL, HttpMethod.POST, entity, ApiError.class, UUID.randomUUID()
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingStoryForNonExistentAnimal() {
        Story story = new Story(randomAlphabetic(300));

        ResponseEntity<ApiError> response = testClient.postForEntity(
                STORY_URL, story, ApiError.class, UUID.randomUUID(), getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingStoryThatAlreadyExist() {
        Animal animal = createAndSaveAnimal();
        Story story = new Story(randomAlphabetic(300));
        testClient.postForEntity(STORY_URL, story, Story.class, animal.getUuid(), getHttpHeaders());

        ResponseEntity<ApiError> conflictResponse = testClient.postForEntity(
                STORY_URL, story, ApiError.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(conflictResponse.getStatusCode(), is(CONFLICT));
        ApiError apiError = conflictResponse.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn200OkWithStory() {
        Animal animal = createAndSaveAnimal();
        ResponseEntity<Story> createdStoryResponse = testClient.postForEntity(
                STORY_URL, new Story(randomAlphabetic(100)), Story.class, animal.getUuid(), getHttpHeaders()
        );
        Story createdStory = createdStoryResponse.getBody();

        ResponseEntity<Story> response = testClient.getForEntity(
                STORY_URL, Story.class, animal.getUuid(), getHttpHeaders()
        );

        assertThat(response.getStatusCode(), is(OK));
        Story foundStory = response.getBody();
        assertThat(foundStory, is(createdStory));
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalUuidDoesNotExist() {
        UUID randomUuid = UUID.randomUUID();

        ResponseEntity<ApiError> response = testClient.getForEntity(STORY_URL, ApiError.class, randomUuid);

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn404NotFoundWhenStoryCannotBeFoundForValidAnimal() {
        Animal animal = createAndSaveAnimal();

        ResponseEntity<ApiError> response = testClient.getForEntity(STORY_URL, ApiError.class, animal.getUuid());

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }
}
