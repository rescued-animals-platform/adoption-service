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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

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

    @Before
    public void runFirst() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(TestUtils.getObjectMapper());
        RestTemplate restTemplate = testClient.getRestTemplate();

        restTemplate.getMessageConverters().removeIf(
                m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName())
        );
        restTemplate.getMessageConverters().add(messageConverter);
    }

    protected static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    protected JpaAnimal createAndSaveJpaAnimal() {
        return jpaAnimalRepository.save(new JpaAnimal(AnimalBuilder.random().build()));
    }

    protected Animal createAndSaveAnimal() {
        Animal animalForAdoption = AnimalBuilder.random().withState(null).build();
        ResponseEntity<Animal> responseEntity = testClient.postForEntity(
                ANIMALS_URL, animalForAdoption, Animal.class, getHttpHeaders()
        );
        assertThat(responseEntity.getStatusCode(), is(CREATED));
        Animal animal = responseEntity.getBody();
        assertNotNull(animal);

        return animal;
    }
}
