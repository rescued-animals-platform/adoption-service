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

package ec.animal.adoption.adapter.rest.resource;

import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateRequest;
import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateResponse;
import ec.animal.adoption.adapter.rest.model.animal.CreateAnimalRequestBuilder;
import ec.animal.adoption.domain.model.state.State;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static java.lang.System.getenv;

public abstract class AbstractApiTest {

    static final String ANIMALS_ADMIN_URL = "/adoption/admin/animals";
    static final String ANIMAL_ADMIN_URL = "/adoption/admin/animals/{id}";
    static final String ANIMALS_URL = "/adoption/animals";
    static final String CHARACTERISTICS_ADMIN_URL = "/adoption/admin/animals/{id}/characteristics";
    static final String CHARACTERISTICS_URL = "/adoption/animals/{id}/characteristics";
    static final String PICTURES_ADMIN_URL = "/adoption/admin/animals/{id}/pictures";
    static final String PICTURES_URL = "/adoption/animals/{id}/pictures";
    static final String STORY_ADMIN_URL = "/adoption/admin/animals/{id}/story";
    static final String STORY_URL = "/adoption/animals/{id}/story";

    static WebTestClient webTestClient;

    @BeforeAll
    public static void setUpClass() {
        String host = getenv("ADOPTION_SERVICE_URL");
        String jwtAccessToken = getenv("JWT_ACCESS_TOKEN");
        webTestClient = WebTestClient.bindToServer()
                                     .defaultHeader("Authorization", "Bearer " + jwtAccessToken)
                                     .baseUrl(host)
                                     .responseTimeout(Duration.ofSeconds(10))
                                     .build();
    }

    AnimalCreateUpdateResponse createRandomAnimalWithDefaultLookingForHumanState() {
        AnimalCreateUpdateRequest animalCreateUpdateRequest = CreateAnimalRequestBuilder.random().withState(State.lookingForHuman()).build();
        return webTestClient.post()
                            .uri(ANIMALS_ADMIN_URL)
                            .bodyValue(animalCreateUpdateRequest)
                            .exchange()
                            .expectStatus()
                            .isCreated()
                            .expectBody(AnimalCreateUpdateResponse.class)
                            .returnResult()
                            .getResponseBody();
    }
}
