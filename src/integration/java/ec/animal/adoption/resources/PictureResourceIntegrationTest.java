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

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.*;

public class PictureResourceIntegrationTest extends AbstractIntegrationTest {

    private UUID animalUuid;
    private HttpHeaders headers;
    private String name;
    private PictureType pictureType;

    @Before
    public void setUp() {
        Animal animal = createAndSaveAnimal();
        animalUuid = animal.getUuid();
        name = randomAlphabetic(10);
        pictureType = PictureType.PRIMARY;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    }

    @Test
    public void shouldReturn201CreatedWithLinkPicture() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("name", name);
        parameters.add("pictureType", pictureType.name());
        parameters.add("largeImage", new ClassPathResource("test-image-large.jpeg"));
        parameters.add("smallImage", new ClassPathResource("test-image-small.jpeg"));

        ResponseEntity<LinkPicture> response = testClient.postForEntity(
                PICTURES_URL, parameters, LinkPicture.class, animalUuid, headers
        );

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        LinkPicture linkPicture = response.getBody();
        assertNotNull(linkPicture);
        assertThat(linkPicture.getAnimalUuid(), is(animalUuid));
        assertThat(linkPicture.getName(), is(name));
        assertThat(linkPicture.getPictureType(), is(pictureType));
        assertNotNull(linkPicture.getLargeImageUrl());
        assertNotNull(linkPicture.getSmallImageUrl());
    }

    @Test
    public void shouldReturn404NotFoundWhenCreatingPictureForNonExistentAnimal() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("name", name);
        parameters.add("pictureType", pictureType.name());
        parameters.add("largeImage", new ClassPathResource("test-image-large.jpeg"));
        parameters.add("smallImage", new ClassPathResource("test-image-small.jpeg"));

        ResponseEntity<ApiError> response = testClient.postForEntity(
                PICTURES_URL, parameters, ApiError.class, UUID.randomUUID(), headers
        );

        assertThat(response.getStatusCode(), is(NOT_FOUND));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingPrimaryPictureThatAlreadyExist() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("name", name);
        parameters.add("pictureType", PictureType.PRIMARY.name());
        parameters.add("largeImage", new ClassPathResource("test-image-large.jpeg"));
        parameters.add("smallImage", new ClassPathResource("test-image-small.jpeg"));
        testClient.postForEntity(
                PICTURES_URL, parameters, LinkPicture.class, animalUuid, headers
        );

        ResponseEntity<ApiError> response = testClient.postForEntity(
                PICTURES_URL, parameters, ApiError.class, animalUuid, headers
        );

        assertThat(response.getStatusCode(), is(CONFLICT));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn400BadRequestForInvalidLargeImageFile() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("name", name);
        parameters.add("pictureType", PictureType.PRIMARY.name());
        parameters.add("largeImage", new ClassPathResource("invalid-image-file.txt"));
        parameters.add("smallImage", new ClassPathResource("test-image-small.jpeg"));

        ResponseEntity<ApiError> response = testClient.postForEntity(
                PICTURES_URL, parameters, ApiError.class, animalUuid, headers
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn400BadRequestForInvalidSmallImageFile() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("name", name);
        parameters.add("pictureType", PictureType.PRIMARY.name());
        parameters.add("largeImage", new ClassPathResource("test-image-large.jpeg"));
        parameters.add("smallImage", new ClassPathResource("invalid-image-file.txt"));

        ResponseEntity<ApiError> response = testClient.postForEntity(
                PICTURES_URL, parameters, ApiError.class, animalUuid, headers
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }
}
