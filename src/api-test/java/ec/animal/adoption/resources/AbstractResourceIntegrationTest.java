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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.state.LookingForHuman;
import org.junit.BeforeClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.System.getenv;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractResourceIntegrationTest {

    static final String ANIMALS_URL = "/adoption/animals";
    static final String CHARACTERISTICS_URL = ANIMALS_URL + "/{animalUuid}/characteristics";
    static final String PICTURES_URL = ANIMALS_URL + "/{animalUuid}/pictures";
    static final String STORY_URL = ANIMALS_URL + "/{animalUuid}/story";

    static WebTestClient webTestClient;

    @BeforeClass
    public static void setUpClass() {
        String host = getenv("ADOPTION_SERVICE_URL");
        webTestClient = WebTestClient.bindToServer().baseUrl(host).responseTimeout(Duration.ofSeconds(10)).build();
    }

    Animal createAndSaveAnimalWithDefaultLookingForHumanState() {
        return webTestClient.post()
                .uri(ANIMALS_URL)
                .syncBody(AnimalBuilder.random().withState(new LookingForHuman(LocalDateTime.now())).build())
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Animal.class)
                .returnResult()
                .getResponseBody();
    }

    LinkPicture createAndSavePrimaryLinkPictureForAnimal(final UUID animalUuid) {
        LinkedMultiValueMap<String, Object> multipartPicturesFormData = new LinkedMultiValueMap<>();
        multipartPicturesFormData.add("name", randomAlphabetic(10));
        multipartPicturesFormData.add("pictureType", PictureType.PRIMARY.name());
        multipartPicturesFormData.add("largeImage", new ClassPathResource("test-image-large.jpeg"));
        multipartPicturesFormData.add("smallImage", new ClassPathResource("test-image-small.jpeg"));

        return webTestClient.post()
                .uri(PICTURES_URL, animalUuid)
                .syncBody(multipartPicturesFormData)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(LinkPicture.class)
                .returnResult()
                .getResponseBody();
    }
}
