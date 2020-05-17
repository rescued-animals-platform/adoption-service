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

import ec.animal.adoption.api.model.animal.AnimalCreateUpdateRequest;
import ec.animal.adoption.api.model.animal.AnimalCreateUpdateResponse;
import ec.animal.adoption.api.model.animal.CreateAnimalRequestBuilder;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.EstimatedAge;
import ec.animal.adoption.domain.animal.Sex;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.StateName;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomEstimatedAge;
import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSex;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomState;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.UseObjectForClearerAPI"})
public class AnimalResourceApiTest extends AbstractApiTest {

    private String clinicalRecord;
    private String name;
    private Species species;
    private Sex sex;
    private EstimatedAge estimatedAge;
    private CreateAnimalRequestBuilder createAnimalRequestBuilder;

    @BeforeEach
    void setUp() {
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        species = getRandomSpecies();
        sex = getRandomSex();
        estimatedAge = getRandomEstimatedAge();
        createAnimalRequestBuilder = CreateAnimalRequestBuilder.random()
                                                               .withClinicalRecord(clinicalRecord)
                                                               .withName(name)
                                                               .withSpecies(species)
                                                               .withSex(sex)
                                                               .withEstimatedAge(estimatedAge);
    }

    @Test
    public void shouldReturn201Created() {
        String adoptionFormId = randomAlphabetic(10);
        State state = State.adopted(adoptionFormId);
        AnimalCreateUpdateRequest animalCreateUpdateRequest = createAnimalRequestBuilder.withState(state).build();

        webTestClient.post()
                     .uri(ANIMALS_ADMIN_URL)
                     .bodyValue(animalCreateUpdateRequest)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .jsonPath("$.id").isNotEmpty()
                     .jsonPath("$.registrationDate").isNotEmpty()
                     .jsonPath("$.clinicalRecord").isEqualTo(clinicalRecord)
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.species").isEqualTo(species.name())
                     .jsonPath("$.estimatedAge").isEqualTo(estimatedAge.name())
                     .jsonPath("$.sex").isEqualTo(sex.name())
                     .jsonPath("$.state[?(@.name == '%s' && @.adoptionFormId == '%s')]",
                               state.getName().name(),
                               adoptionFormId)
                     .exists();
    }

    @Test
    public void shouldReturn201CreatedSettingLookingForHumanAsDefaultStateWhenStateIsNotSent() {
        AnimalCreateUpdateRequest animalCreateUpdateRequest = createAnimalRequestBuilder.withState(null).build();

        webTestClient.post()
                     .uri(ANIMALS_ADMIN_URL)
                     .bodyValue(animalCreateUpdateRequest)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .jsonPath("$.id").isNotEmpty()
                     .jsonPath("$.registrationDate").isNotEmpty()
                     .jsonPath("$.clinicalRecord").isEqualTo(clinicalRecord)
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.species").isEqualTo(species.name())
                     .jsonPath("$.estimatedAge").isEqualTo(estimatedAge.name())
                     .jsonPath("$.sex").isEqualTo(sex.name())
                     .jsonPath("$.state.name").isEqualTo(StateName.LOOKING_FOR_HUMAN.name());
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAnimalAndJsonCannotBeParsed() throws JSONException {
        String invalidSex = randomAlphabetic(10);
        String createAnimalRequestWithWrongData = new JSONObject()
                .put("clinicalRecord", clinicalRecord)
                .put("name", name)
                .put("species", species)
                .put("estimatedAge", estimatedAge)
                .put("sex", invalidSex)
                .toString();

        webTestClient.post()
                     .uri(ANIMALS_ADMIN_URL)
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(createAnimalRequestWithWrongData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Malformed JSON request")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenCreatingAnimalAndMissingDataIsProvided() throws JSONException {
        String createAnimalRequestWithMissingData = new JSONObject()
                .put("clinicalRecord", clinicalRecord)
                .put("name", name)
                .put("species", species)
                .toString();

        webTestClient.post()
                     .uri(ANIMALS_ADMIN_URL)
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(createAnimalRequestWithMissingData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Validation failed")
                     .jsonPath("$.subErrors").isNotEmpty();
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingAnAnimalThatAlreadyExists() {
        AnimalCreateUpdateRequest animalCreateUpdateRequest = createAnimalRequestBuilder.build();
        webTestClient.post()
                     .uri(ANIMALS_ADMIN_URL)
                     .bodyValue(animalCreateUpdateRequest)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        webTestClient.post()
                     .uri(ANIMALS_ADMIN_URL)
                     .bodyValue(animalCreateUpdateRequest)
                     .exchange()
                     .expectStatus()
                     .isEqualTo(CONFLICT)
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(CONFLICT.name())
                     .jsonPath("$.message").isEqualTo("The resource already exists")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    void shouldReturn200OkWithUpdatedAnimal() {
        AnimalCreateUpdateResponse createdAnimalResponse = createRandomAnimalWithDefaultLookingForHumanState();
        UUID animalId = createdAnimalResponse.getAnimalId();
        String expectedRegistrationDate = createdAnimalResponse.getRegistrationDate().toString();
        String notes = randomAlphabetic(10);
        State state = State.unavailable(notes);
        AnimalCreateUpdateRequest updateAnimalRequest = createAnimalRequestBuilder.withState(state).build();

        webTestClient.put()
                     .uri(ANIMAL_ADMIN_URL, animalId)
                     .bodyValue(updateAnimalRequest)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.id").isEqualTo(animalId.toString())
                     .jsonPath("$.registrationDate").isEqualTo(expectedRegistrationDate)
                     .jsonPath("$.clinicalRecord").isEqualTo(clinicalRecord)
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.species").isEqualTo(species.name())
                     .jsonPath("$.estimatedAge").isEqualTo(estimatedAge.name())
                     .jsonPath("$.sex").isEqualTo(sex.name())
                     .jsonPath("$.state[?(@.name == '%s' && @.notes == '%s')]",
                               state.getName().name(),
                               notes)
                     .exists();
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAnAnimalAndJsonCannotBeParsed() throws JSONException {
        AnimalCreateUpdateResponse createdAnimalResponse = createRandomAnimalWithDefaultLookingForHumanState();
        String invalidEstimatedAge = randomAlphabetic(10);
        String updateAnimalRequestWithWrongData = new JSONObject()
                .put("clinicalRecord", clinicalRecord)
                .put("name", name)
                .put("species", species)
                .put("estimatedAge", invalidEstimatedAge)
                .put("sex", sex)
                .toString();

        webTestClient.put()
                     .uri(ANIMAL_ADMIN_URL, createdAnimalResponse.getAnimalId())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(updateAnimalRequestWithWrongData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Malformed JSON request")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAnimalAndMissingDataIsProvided() throws JSONException {
        AnimalCreateUpdateResponse createdAnimalResponse = createRandomAnimalWithDefaultLookingForHumanState();
        String updateAnimalRequestWithMissingData = new JSONObject()
                .put("clinicalRecord", clinicalRecord)
                .put("name", name)
                .put("sex", sex)
                .toString();

        webTestClient.put()
                     .uri(ANIMAL_ADMIN_URL, createdAnimalResponse.getAnimalId())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(updateAnimalRequestWithMissingData)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").isEqualTo("Validation failed")
                     .jsonPath("$.subErrors").isNotEmpty();
    }

    @Test
    public void shouldReturn400BadRequestWhenUpdatingAnimalWithClinicalRecordFromAnotherAnimalThatAlreadyExists() {
        AnimalCreateUpdateResponse createdAnimalWithClinicalRecord = createRandomAnimalWithDefaultLookingForHumanState();
        String clinicalRecord = createdAnimalWithClinicalRecord.getClinicalRecord();
        AnimalCreateUpdateResponse createdAnimalResponse = createRandomAnimalWithDefaultLookingForHumanState();
        AnimalCreateUpdateRequest updateAnimalRequest = createAnimalRequestBuilder.withClinicalRecord(clinicalRecord).build();

        webTestClient.put()
                     .uri(ANIMAL_ADMIN_URL, createdAnimalResponse.getAnimalId())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(updateAnimalRequest)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(BAD_REQUEST.name())
                     .jsonPath("$.message").value(matchesPattern("^Can't update animal with clinical record.*"))
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn404NotFoundWhenUpdatingAnimalThatDoesNotExist() {
        webTestClient.put()
                     .uri(ANIMAL_ADMIN_URL, UUID.randomUUID())
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(createAnimalRequestBuilder.build())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn200OkWithAnimal() {
        AnimalCreateUpdateResponse animalCreateUpdateResponse = createAnimal(createAnimalRequestBuilder.build());

        webTestClient.get()
                     .uri(ANIMAL_ADMIN_URL, animalCreateUpdateResponse.getAnimalId())
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.id").isEqualTo(animalCreateUpdateResponse.getAnimalId().toString())
                     .jsonPath("$.clinicalRecord").isEqualTo(clinicalRecord)
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.species").isEqualTo(species.name())
                     .jsonPath("$.sex").isEqualTo(sex.name())
                     .jsonPath("$.estimatedAge").isEqualTo(estimatedAge.name());
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalDoesNotExist() {
        webTestClient.get()
                     .uri(ANIMAL_ADMIN_URL, UUID.randomUUID())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody()
                     .jsonPath("$.status").isEqualTo(NOT_FOUND.name())
                     .jsonPath("$.message").isEqualTo("Unable to find the resource")
                     .jsonPath("$.subErrors").doesNotExist();
    }

    @Test
    public void shouldReturn200OkWithPagedEntityContainingFirstPageAndTwoAnimals() {
        createRandomAnimalWithDefaultLookingForHumanState();
        createRandomAnimalWithDefaultLookingForHumanState();

        webTestClient.get()
                     .uri(ANIMALS_ADMIN_URL + "?page=0&size=2")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(PagedEntity.class)
                     .consumeWith(entity -> {
                         var pagedEntity = entity.getResponseBody();
                         assertNotNull(pagedEntity);
                         assertThat(pagedEntity.isEmpty(), is(false));
                         assertThat(pagedEntity.getNumberOfElements(), is(2));
                         assertThat(pagedEntity.getSize(), is(2));
                         assertThat(pagedEntity.isFirst(), is(true));
                         assertThat(pagedEntity.getContent().size(), is(2));
                     });
    }

    @Test
    public void shouldReturn200OkWithPagedEntityContainingFirstPageAndThreeAnimalsFiltered() {
        State state = getRandomState();
        Species species = getRandomSpecies();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        Size size = getRandomSize();
        int numberOfAnimals = 3;
        for (int n = 1; n <= numberOfAnimals; n++) {
            UUID animalId = createAnimal(CreateAnimalRequestBuilder.random()
                                                                   .withState(state)
                                                                   .withSpecies(species)
                                                                   .build()).getAnimalId();
            createCharacteristics(animalId, CharacteristicsFactory.random()
                                                                  .withPhysicalActivity(physicalActivity)
                                                                  .withSize(size)
                                                                  .build());
        }
        String uri = ANIMALS_URL + "?state={state}&species={species}&physicalActivity={physicalActivity}" +
                "&animalSize={size}&page=0&size=3";

        webTestClient.get()
                     .uri(uri, state.getName().name(), species.name(), physicalActivity.name(), size.name())
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(PagedEntity.class)
                     .consumeWith(entity -> {
                         var pagedEntity = entity.getResponseBody();
                         assertNotNull(pagedEntity);
                         assertThat(pagedEntity.isEmpty(), is(false));
                         assertThat(pagedEntity.getNumberOfElements(), is(numberOfAnimals));
                         assertThat(pagedEntity.getSize(), is(numberOfAnimals));
                         assertThat(pagedEntity.isFirst(), is(true));
                         assertThat(pagedEntity.getContent().size(), is(numberOfAnimals));
                     });
    }

    @ParameterizedTest(name = "{index} returns BAD_REQUEST for filters [state: {0}, species: {1}, physicalActivity: {2}, size: {3}]")
    @CsvSource(value = {"invalidState,DOG,LOW,TINY",
                        "ADOPTED,invalidSpecies,HIGH,SMALL",
                        "UNAVAILABLE,CAT,invalidPhysicalActivity,MEDIUM",
                        "LOOKING_FOR_HUMAN,DOG,MEDIUM,invalidSize"},
               delimiter = ',')
    public void shouldReturn400BadRequestWhenFilterIsInvalid(final String state,
                                                             final String species,
                                                             final String physicalActivity,
                                                             final String size) {
        String uri = ANIMALS_URL + "?state={state}&species={species}&physicalActivity={physicalActivity}" +
                "&animalSize={size}&page=0&size=3";

        webTestClient.get()
                     .uri(uri, state, species, physicalActivity, size)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .isEmpty();
    }

    private static AnimalCreateUpdateResponse createAnimal(final AnimalCreateUpdateRequest animalCreateUpdateRequest) {
        AnimalCreateUpdateResponse animalCreateUpdateResponse = webTestClient.post()
                                                                             .uri(ANIMALS_ADMIN_URL)
                                                                             .bodyValue(animalCreateUpdateRequest)
                                                                             .exchange()
                                                                             .expectStatus()
                                                                             .isCreated()
                                                                             .expectBody(AnimalCreateUpdateResponse.class)
                                                                             .returnResult()
                                                                             .getResponseBody();
        assertNotNull(animalCreateUpdateResponse);

        return animalCreateUpdateResponse;
    }

    private static void createCharacteristics(final UUID animalId, final Characteristics characteristics) {
        webTestClient.post()
                     .uri(CHARACTERISTICS_ADMIN_URL, animalId)
                     .bodyValue(characteristics)
                     .exchange()
                     .expectStatus()
                     .isCreated();
    }
}