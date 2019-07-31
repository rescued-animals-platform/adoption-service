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
import org.junit.BeforeClass;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static java.lang.System.getenv;

@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class ResourceIntegrationTest {

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

    Animal createAndSaveAnimal() {
        Animal animalForAdoption = AnimalBuilder.random().withState(null).build();

        EntityExchangeResult<Animal> animalEntityExchangeResult = webTestClient.post().uri(ANIMALS_URL).
                syncBody(animalForAdoption).exchange().
                expectStatus().isCreated().expectBody(Animal.class).returnResult();

        return animalEntityExchangeResult.getResponseBody();
    }
}