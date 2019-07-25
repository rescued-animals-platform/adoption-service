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
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;

@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
public class PictureResourceIntegrationTest extends ResourceIntegrationTest {

    private UUID animalUuid;
    private String name;
    private PictureType pictureType;
    private LinkedMultiValueMap<String, Object> validMultipartPicturesFormData;

    @Before
    public void setUp() {
        Animal animal = createAndSaveAnimal();
        animalUuid = animal.getUuid();
        name = randomAlphabetic(10);
        pictureType = PictureType.PRIMARY;
        validMultipartPicturesFormData = new LinkedMultiValueMap<>();
        validMultipartPicturesFormData.add("name", name);
        validMultipartPicturesFormData.add("pictureType", pictureType.name());
        validMultipartPicturesFormData.add("largeImage", new ClassPathResource("test-image-large.jpeg"));
        validMultipartPicturesFormData.add("smallImage", new ClassPathResource("test-image-small.jpeg"));
    }

    @Test
    public void shouldReturn201CreatedWithLinkPicture() {
        webTestClient.post()
                .uri(PICTURES_URL, animalUuid)
                .syncBody(validMultipartPicturesFormData)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(LinkPicture.class)
                .consumeWith(linkPictureEntityExchangeResult -> {
                    LinkPicture linkPicture = linkPictureEntityExchangeResult.getResponseBody();
                    assertNotNull(linkPicture);
                    assertThat(linkPicture.getAnimalUuid(), is(animalUuid));
                    assertThat(linkPicture.getName(), is(name));
                    assertThat(linkPicture.getPictureType(), is(pictureType));
                });
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingPictureForNonExistentAnimal() {
        webTestClient.post()
                .uri(PICTURES_URL, UUID.randomUUID())
                .syncBody(validMultipartPicturesFormData)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingPrimaryPictureThatAlreadyExist() {
        webTestClient.post()
                .uri(PICTURES_URL, animalUuid)
                .syncBody(validMultipartPicturesFormData)
                .exchange()
                .expectStatus()
                .isCreated();

        webTestClient.post()
                .uri(PICTURES_URL, animalUuid)
                .syncBody(validMultipartPicturesFormData)
                .exchange()
                .expectStatus()
                .isEqualTo(CONFLICT)
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestForInvalidLargeImageFile() {
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add("name", name);
        invalidMultipartPicturesFormData.add("pictureType", PictureType.PRIMARY.name());
        invalidMultipartPicturesFormData.add("largeImage", new ClassPathResource("invalid-image-file.txt"));
        invalidMultipartPicturesFormData.add("smallImage", new ClassPathResource("test-image-small.jpeg"));

        webTestClient.post()
                .uri(PICTURES_URL, animalUuid)
                .syncBody(invalidMultipartPicturesFormData)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestForInvalidSmallImageFile() {
        LinkedMultiValueMap<String, Object> invalidMultipartPicturesFormData = new LinkedMultiValueMap<>();
        invalidMultipartPicturesFormData.add("name", name);
        invalidMultipartPicturesFormData.add("pictureType", PictureType.PRIMARY.name());
        invalidMultipartPicturesFormData.add("largeImage", new ClassPathResource("test-image-large.jpeg"));
        invalidMultipartPicturesFormData.add("smallImage", new ClassPathResource("invalid-image-file.txt"));

        webTestClient.post()
                .uri(PICTURES_URL, animalUuid)
                .syncBody(invalidMultipartPicturesFormData)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithPrimaryPicture() {
        LinkPicture createdLinkPicture = webTestClient.post()
                .uri(PICTURES_URL, animalUuid)
                .syncBody(validMultipartPicturesFormData)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(LinkPicture.class)
                .returnResult()
                .getResponseBody();

        webTestClient.get()
                .uri(PICTURES_URL, animalUuid)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(LinkPicture.class)
                .isEqualTo(createdLinkPicture);
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalUuidDoesNotExist() {
        webTestClient.get()
                .uri(PICTURES_URL, UUID.randomUUID())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn404NotFoundWhenPrimaryPictureCannotBeFoundForValidAnimal() {
        webTestClient.get()
                .uri(PICTURES_URL, animalUuid)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }
}
