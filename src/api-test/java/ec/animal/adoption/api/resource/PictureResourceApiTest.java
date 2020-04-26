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

import ec.animal.adoption.api.model.error.ApiError;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CONFLICT;

public class PictureResourceApiTest extends AbstractApiTest {

    private static final String NAME = "name";
    private static final String PICTURE_TYPE = "pictureType";
    private static final String LARGE_IMAGE = "largeImage";
    private static final String SMALL_IMAGE = "smallImage";

    private UUID animalUuid;
    private String name;
    private PictureType pictureType;
    private LinkedMultiValueMap<String, Object> validMultipartPicturesFormData;

    @BeforeEach
    public void setUp() {
        Animal animal = createRandomAnimalWithDefaultLookingForHumanState();
        animalUuid = animal.getUuid();
        name = randomAlphabetic(10);
        pictureType = PictureType.PRIMARY;
        validMultipartPicturesFormData = new LinkedMultiValueMap<>();
        validMultipartPicturesFormData.add(NAME, name);
        validMultipartPicturesFormData.add(PICTURE_TYPE, pictureType.name());
        validMultipartPicturesFormData.add(LARGE_IMAGE, new ClassPathResource("test-image-large.jpeg"));
        validMultipartPicturesFormData.add(SMALL_IMAGE, new ClassPathResource("test-image-small.jpeg"));
    }

    @Test
    public void shouldReturn201CreatedWithLinkPicture() {
        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalUuid)
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody(LinkPicture.class)
                     .consumeWith(linkPictureEntityExchangeResult -> {
                         LinkPicture linkPicture = linkPictureEntityExchangeResult.getResponseBody();
                         assertNotNull(linkPicture);
                         assertThat(linkPicture.getName(), is(name));
                         assertThat(linkPicture.getPictureType(), is(pictureType));
                         assertNotNull(linkPicture.getLargeImageUrl());
                         assertNotNull(linkPicture.getSmallImageUrl());
                     });
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingPictureForNonExistentAnimal() {
        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, UUID.randomUUID())
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingPrimaryPictureThatAlreadyExist() {
        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalUuid)
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalUuid)
                     .bodyValue(validMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isEqualTo(CONFLICT)
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAPictureThatIsNotPrimary() {
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE, PictureType.ALTERNATE.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE, new ClassPathResource("test-image-large.jpeg"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE, new ClassPathResource("test-image-small.jpeg"));

        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalUuid)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestForInvalidLargeImageFile() {
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE, PictureType.PRIMARY.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE, new ClassPathResource("invalid-image-file.txt"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE, new ClassPathResource("test-image-small.jpeg"));

        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalUuid)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestForInvalidSmallImageFile() {
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add(NAME, name);
        invalidMultipartPicturesFormData.add(PICTURE_TYPE, PictureType.PRIMARY.name());
        invalidMultipartPicturesFormData.add(LARGE_IMAGE, new ClassPathResource("test-image-large.jpeg"));
        invalidMultipartPicturesFormData.add(SMALL_IMAGE, new ClassPathResource("invalid-image-file.txt"));

        webTestClient.post()
                     .uri(PICTURES_ADMIN_URL, animalUuid)
                     .bodyValue(invalidMultipartPicturesFormData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithPrimaryPicture() {
        LinkPicture createdLinkPicture = webTestClient.post()
                                                      .uri(PICTURES_ADMIN_URL, animalUuid)
                                                      .bodyValue(validMultipartPicturesFormData)
                                                      .exchange()
                                                      .expectStatus()
                                                      .isCreated()
                                                      .expectBody(LinkPicture.class)
                                                      .returnResult()
                                                      .getResponseBody();

        assertNotNull(createdLinkPicture);

        webTestClient.get()
                     .uri(GET_PICTURES_URL, animalUuid)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(LinkPicture.class)
                     .isEqualTo(createdLinkPicture);
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalUuidDoesNotExist() {
        webTestClient.get()
                     .uri(GET_PICTURES_URL, UUID.randomUUID())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenPrimaryPictureCannotBeFoundForValidAnimal() {
        webTestClient.get()
                     .uri(GET_PICTURES_URL, animalUuid)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiError.class);
    }
}
