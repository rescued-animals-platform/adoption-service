package ec.animal.adoption;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class IntegrationTest {

    protected static final String ANIMALS_URL = "/adoption/animals";
    protected static final String CHARACTERISTICS_URL = ANIMALS_URL + "/{animalUuid}/characteristics";

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    protected ResponseEntity<Animal> createAndSaveAnimal() {
        String clinicalRecord = randomAlphabetic(10);
        String name = randomAlphabetic(10);
        LocalDateTime registrationDate = LocalDateTime.now();
        Type type = IntegrationTestUtils.getRandomType();
        EstimatedAge estimatedAge = IntegrationTestUtils.getRandomEstimatedAge();
        Sex sex = IntegrationTestUtils.getRandomSex();
        State lookingForHumanState = new LookingForHuman(registrationDate);
        Animal animalForAdoption = new Animal(
                clinicalRecord, name, registrationDate, type, estimatedAge, sex, lookingForHumanState
        );

        return testRestTemplate.postForEntity(
                ANIMALS_URL, animalForAdoption, Animal.class, getHttpHeaders()
        );
    }
}
