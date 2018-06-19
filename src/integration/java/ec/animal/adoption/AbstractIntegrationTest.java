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

package ec.animal.adoption;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Species;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static ec.animal.adoption.TestUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.CREATED;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractIntegrationTest {

    protected static final String ANIMALS_URL = "/adoption/animals";
    protected static final String CHARACTERISTICS_URL = ANIMALS_URL + "/{animalUuid}/characteristics";
    protected static final String PICTURES_URL = ANIMALS_URL + "/{animalUuid}/pictures";
    protected static final String STORY_URL = ANIMALS_URL + "/{animalUuid}/story";

    @Autowired
    protected TestRestTemplate testClient;

    @Autowired
    private JpaAnimalRepository jpaAnimalRepository;

    protected static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    protected JpaAnimal createAndSaveJpaAnimal() {
        return jpaAnimalRepository.save(new JpaAnimal(new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(),
                Species.CAT,
                EstimatedAge.YOUNG_ADULT,
                Sex.MALE,
                new Unavailable(randomAlphabetic(10))
        )));
    }

    protected Animal createAndSaveAnimal() {
        String clinicalRecord = randomAlphabetic(10);
        String name = randomAlphabetic(10);
        LocalDateTime registrationDate = LocalDateTime.now();
        Species species = getRandomSpecies();
        EstimatedAge estimatedAge = getRandomEstimatedAge();
        Sex sex = getRandomSex();
        State lookingForHumanState = new LookingForHuman(registrationDate);
        Animal animalForAdoption = new Animal(
                clinicalRecord, name, registrationDate, species, estimatedAge, sex, lookingForHumanState
        );

        ResponseEntity<Animal> responseEntity = testClient.postForEntity(
                ANIMALS_URL, animalForAdoption, Animal.class, getHttpHeaders()
        );
        assertThat(responseEntity.getStatusCode(), is(CREATED));
        Animal animal = responseEntity.getBody();
        assertNotNull(animal);

        return animal;
    }
}
