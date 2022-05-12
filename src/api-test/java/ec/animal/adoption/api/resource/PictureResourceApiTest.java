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
import ec.animal.adoption.api.model.media.LinkPictureResponse;
import ec.animal.adoption.domain.media.PictureType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.*;

public class PictureResourceApiTest extends AbstractApiTest {

    private static final String NAME_KEY = "name";
    private static final String PICTURE_TYPE_KEY = "pictureType";
    private static final String LARGE_IMAGE_KEY = "largeImage";
    private static final String SMALL_IMAGE_KEY = "smallImage";

    private UUID animalId;
    private String name;
    private PictureType pictureType;
    private LinkedMultiValueMap<String, Object> validMultipartPicturesFormData;

    @BeforeEach
    public void setUp() {
        AnimalCreateUpdateResponse animalCreateUpdateResponse = createRandomAnimalWithDefaultLookingForHumanState();
        animalId = animalCreateUpdateResponse.getAnimalId();
        name = randomAlphabetic(10);
        pictureType = PictureType.PRIMARY;
        validMultipartPicturesFormData = new LinkedMultiValueMap<>();
        validMultipartPicturesFormData.add(NAME_KEY, name);
        validMultipartPicturesFormData.add(PICTURE_TYPE_KEY, pictureType.name());
        validMultipartPicturesFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("test-image-large.jpeg"));
        validMultipartPicturesFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("test-image-small.jpeg"));
    }

    @Test
    public void shouldReturn201CreatedWithLinkPicture() {
        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.pictureType").isEqualTo(pictureType.name())
                     .jsonPath("$.largeImageMediaLink.url").isNotEmpty()
                     .jsonPath("$.smallImageMediaLink.url").isNotEmpty();
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAPictureThatIsNotPrimary() {
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME_KEY, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE_KEY, PictureType.ALTERNATE.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("test-image-large.jpeg"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("test-image-small.jpeg"));

        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isNotEmpty()
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAPictureWithInvalidLargeImageFile() {
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME_KEY, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE_KEY, PictureType.PRIMARY.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("invalid-image-file.txt"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("test-image-small.jpeg"));

        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isNotEmpty()
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAPictureWithInvalidSmallImageFile() {
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME_KEY, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE_KEY, PictureType.PRIMARY.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("test-image-large.jpeg"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("invalid-image-file.txt"));

        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isNotEmpty()
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingPictureForNonExistentAnimal() {
        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingPrimaryPictureThatAlreadyExist() {
        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isEqualTo(CONFLICT)
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(CONFLICT.name())
                     .jsonPath("$.message").isEqualTo("The resource already exists")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn200OkWithUpdatedLinkPicture() {
        createPrimaryPictureForAnimal(animalId);
        String name = randomAlphabetic(10);
        PictureType pictureType = PictureType.PRIMARY;
        LinkedMultiValueMap<String, Object> updateRequestFormData = new LinkedMultiValueMap<>();
        updateRequestFormData.add(NAME_KEY, name);
        updateRequestFormData.add(PICTURE_TYPE_KEY, pictureType.name());
        updateRequestFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("test-image-large.jpeg"));
        updateRequestFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("test-image-small.jpeg"));

        webTestClient.put()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(updateRequestFormData)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.pictureType").isEqualTo(pictureType.name())
                     .jsonPath("$.largeImageMediaLink.url").isNotEmpty()
                     .jsonPath("$.smallImageMediaLink.url").isNotEmpty();
    }

    @Test
    public void shouldReturn200OkWhenUpdatingLinkPictureBeforeCreatingIt() {
        String name = randomAlphabetic(10);
        PictureType pictureType = PictureType.PRIMARY;
        LinkedMultiValueMap<String, Object> updateRequestFormData = new LinkedMultiValueMap<>();
        updateRequestFormData.add(NAME_KEY, name);
        updateRequestFormData.add(PICTURE_TYPE_KEY, pictureType.name());
        updateRequestFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("test-image-large.jpeg"));
        updateRequestFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("test-image-small.jpeg"));

        webTestClient.put()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(updateRequestFormData)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.pictureType").isEqualTo(pictureType.name())
                     .jsonPath("$.largeImageMediaLink.url").isNotEmpty()
                     .jsonPath("$.smallImageMediaLink.url").isNotEmpty();
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAPictureThatIsNotPrimary() {
        createPrimaryPictureForAnimal(animalId);
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME_KEY, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE_KEY, PictureType.ALTERNATE.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("test-image-large.jpeg"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("test-image-small.jpeg"));

        webTestClient.put()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isNotEmpty()
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAPictureWithInvalidLargeImageFile() {
        createPrimaryPictureForAnimal(animalId);
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME_KEY, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE_KEY, PictureType.PRIMARY.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("invalid-image-file.txt"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("test-image-small.jpeg"));

        webTestClient.put()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isNotEmpty()
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAPictureWithInvalidSmallImageFile() {
        createPrimaryPictureForAnimal(animalId);
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME_KEY, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE_KEY, PictureType.PRIMARY.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE_KEY, new ClassPathResource("test-image-large.jpeg"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE_KEY, new ClassPathResource("invalid-image-file.txt"));

        webTestClient.put()
                     .uri(PICTURES_ADMIN_URL, animalId)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isNotEmpty()
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn404NotFoundWhenUpdatingPictureForNonExistentAnimal() {
        webTestClient.put()
                     .uri(PICTURES_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn200OkWithPrimaryPicture() {
        LinkPictureResponse createdLinkPictureResponse = createPrimaryPictureForAnimal(animalId);

        webTestClient.get()
                     .uri(PICTURES_URL, animalId)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(LinkPictureResponse.class)
                     .consumeWith(entity -> {
                         LinkPictureResponse foundLinkPictureResponse = entity.getResponseBody();
                         Assertions.assertThat(foundLinkPictureResponse)
                                   .usingRecursiveComparison()
                                   .isEqualTo(createdLinkPictureResponse);
                     });
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalIdDoesNotExist() {
        webTestClient.get()
                     .uri(PICTURES_URL, UUID.randomUUID())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn404NotFoundWhenPrimaryPictureCannotBeFoundForValidAnimal() {
        webTestClient.get()
                     .uri(PICTURES_URL, animalId)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    private LinkPictureResponse createPrimaryPictureForAnimal(final UUID animalId) {
        LinkPictureResponse createdLinkPictureResponse = webTestClient.post()
                                                                      .uri(PICTURES_ADMIN_URL, animalId)
                                                                      .bodyValue(validMultipartPicturesFormData)
                                                                      .exchange()
                                                                      .expectStatus()
                                                                      .isCreated()
                                                                      .expectBody(LinkPictureResponse.class)
                                                                      .returnResult()
                                                                      .getResponseBody();
        assertNotNull(createdLinkPictureResponse);

        return createdLinkPictureResponse;
    }
}
