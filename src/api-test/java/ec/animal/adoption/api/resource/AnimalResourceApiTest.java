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
import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomState;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CONFLICT;

public class AnimalResourceApiTest extends AbstractApiTest {

    @Test
    public void shouldReturn201Created() {
        Animal animal = AnimalBuilder.random().build();

        webTestClient.post()
                     .uri(CREATE_ANIMAL_URL, AnimalBuilder.DEFAULT_ORGANIZATION_UUID)
                     .bodyValue(animal)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody(Animal.class)
                     .consumeWith(animalEntityExchangeResult -> {
                         Animal createdAnimal = animalEntityExchangeResult.getResponseBody();
                         assertNotNull(createdAnimal);
                         assertAll(
                                 () -> assertNotNull(createdAnimal.getUuid()),
                                 () -> assertNotNull(createdAnimal.getRegistrationDate()),
                                 () -> assertEquals(animal.getClinicalRecord(), createdAnimal.getClinicalRecord()),
                                 () -> assertEquals(animal.getName(), createdAnimal.getName()),
                                 () -> assertEquals(animal.getSpecies(), createdAnimal.getSpecies()),
                                 () -> assertEquals(animal.getEstimatedAge(), createdAnimal.getEstimatedAge()),
                                 () -> assertThat(createdAnimal.getState(), instanceOf(animal.getState().getClass()))
                         );
                     });
    }

    @Test
    public void shouldReturn201CreatedSettingLookingForHumanAsDefaultStateWhenStateIsNotSent() {
        Animal animal = AnimalBuilder.random().withState(null).build();

        webTestClient.post()
                     .uri(CREATE_ANIMAL_URL, AnimalBuilder.DEFAULT_ORGANIZATION_UUID)
                     .bodyValue(animal)
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody(Animal.class)
                     .consumeWith(animalEntityExchangeResult -> {
                         Animal createdAnimal = animalEntityExchangeResult.getResponseBody();
                         assertNotNull(createdAnimal);
                         assertNotNull(createdAnimal.getUuid());
                         assertNotNull(createdAnimal.getRegistrationDate());
                         assertThat(createdAnimal.getClinicalRecord(), is(animal.getClinicalRecord()));
                         assertThat(createdAnimal.getName(), is(animal.getName()));
                         assertThat(createdAnimal.getSpecies(), is(animal.getSpecies()));
                         assertThat(createdAnimal.getEstimatedAge(), is(animal.getEstimatedAge()));
                         assertThat(createdAnimal.getState(), is(instanceOf(LookingForHuman.class)));
                     });
    }

    @ParameterizedTest(name = "should return 400 bad request when organizationId is \"{0}\"")
    @MethodSource("invalidOrganizationIds")
    public void shouldReturn400BadRequestWhenOrganizationIdIsNullOrEmpty(final String organizationId) {
        Animal animal = AnimalBuilder.random().build();

        webTestClient.post()
                     .uri(CREATE_ANIMAL_URL, organizationId)
                     .bodyValue(animal)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static Stream<String> invalidOrganizationIds() {
        return Stream.of(null, "");
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        Animal animal = AnimalBuilder.random().build();
        String wrongRegistrationDate = randomAlphabetic(10);
        String animalWithWrongData = "{\"clinicalRecord\":\"" + animal.getClinicalRecord() +
                "\",\"name\":\"" + animal.getName() + "\",\"registrationDate\":\"" + wrongRegistrationDate +
                "\",\"species\":\"" + animal.getSpecies() + "\",\"estimatedAge\":\"" + animal.getEstimatedAge() +
                "\",\"sex\":\"12345\"}";

        webTestClient.post()
                     .uri(CREATE_ANIMAL_URL, AnimalBuilder.DEFAULT_ORGANIZATION_UUID)
                     .bodyValue(animalWithWrongData)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        Animal animal = AnimalBuilder.random().build();
        String animalWithMissingData = "{\"clinicalRecord\":\"\",\"name\":\"" + animal.getName() +
                "\",\"species\":\"" + animal.getSpecies() + "\"}";

        webTestClient.post()
                     .uri(CREATE_ANIMAL_URL, AnimalBuilder.DEFAULT_ORGANIZATION_UUID)
                     .bodyValue(animalWithMissingData)
                     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingAnAnimalThatAlreadyExists() {
        Animal animal = AnimalBuilder.random().build();
        webTestClient.post()
                     .uri(CREATE_ANIMAL_URL, AnimalBuilder.DEFAULT_ORGANIZATION_UUID)
                     .bodyValue(animal)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        webTestClient.post()
                     .uri(CREATE_ANIMAL_URL, AnimalBuilder.DEFAULT_ORGANIZATION_UUID)
                     .bodyValue(animal)
                     .exchange()
                     .expectStatus()
                     .isEqualTo(CONFLICT)
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithAnimal() {
        Animal createdAnimal = createRandomAnimalWithDefaultLookingForHumanState();

        webTestClient.get()
                     .uri(GET_ANIMAL_URL, createdAnimal.getUuid())
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Animal.class)
                     .consumeWith(animalEntityExchangeResult -> {
                         Animal foundAnimal = animalEntityExchangeResult.getResponseBody();
                         Assertions.assertThat(foundAnimal)
                                   .usingRecursiveComparison()
                                   .isEqualTo(createdAnimal);
                     });
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalDoesNotExist() {
        webTestClient.get()
                     .uri(GET_ANIMAL_URL, UUID.randomUUID())
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithPagedEntityContainingFirstPageAndThreeAnimalsFilteredByState() {
        State state = getRandomState();
        Species species = getRandomSpecies();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        Size size = getRandomSize();
        IntStream.rangeClosed(1, 3).forEach(n -> createAnimal(AnimalBuilder.random().withState(state)
                                                                           .withSpecies(species).withCharacteristics(
                        CharacteristicsBuilder.random().withPhysicalActivity(physicalActivity).withSize(size).build())
                                                                           .withPrimaryLinkPicture(LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build())
                                                                           .build()));
        String uri = GET_ANIMALS_URL + "?state={state}&species={species}&physicalActivity={physicalActivity}" +
                "&animalSize={size}&page=0&size=3";

        webTestClient.get()
                     .uri(uri, state.getStateName(), species, physicalActivity, size)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(PagedEntity.class)
                     .consumeWith(animalsEntityExchangeResult -> {
                         var pagedEntity = animalsEntityExchangeResult.getResponseBody();
                         assertNotNull(pagedEntity);
                         assertThat(pagedEntity.isEmpty(), is(false));
                         assertThat(pagedEntity.getNumberOfElements(), is(3));
                         assertThat(pagedEntity.getSize(), is(3));
                         assertThat(pagedEntity.isFirst(), is(true));
                         assertThat(pagedEntity.getContent().size(), is(3));
                     });
    }

    @Test
    public void shouldReturn400BadRequestWhenStateIsInvalid() {
        String invalidStateName = randomAlphabetic(10);
        String uri = GET_ANIMALS_URL + "?state={state}&species=DOG&physicalActivity=LOW&animalSize=TINY&page=0&size=3";

        webTestClient.get()
                     .uri(uri, invalidStateName)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenSpeciesIsInvalid() {
        String invalidSpecies = randomAlphabetic(10);
        String uri = GET_ANIMALS_URL + "?state=adopted&species={species}&physicalActivity=LOW&animalSize=TINY&page=0&size=3";

        webTestClient.get()
                     .uri(uri, invalidSpecies)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenPhysicalActivityIsInvalid() {
        String invalidPhysicalActivity = randomAlphabetic(10);
        String uri = GET_ANIMALS_URL + "?state=adopted&species=CAT&physicalActivity={physicalActivity}" +
                "&animalSize=TINY&page=0&size=3";

        webTestClient.get()
                     .uri(uri, invalidPhysicalActivity)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn400BadRequestWhenAnimalSizeIsInvalid() {
        String invalidAnimalSize = randomAlphabetic(10);
        String uri = GET_ANIMALS_URL + "?state=adopted&species=CAT&physicalActivity=LOW&animalSize={animalSize}" +
                "&page=0&size=3";

        webTestClient.get()
                     .uri(uri, invalidAnimalSize)
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithPagedEntityContainingFirstPageAndTwoAnimals() {
        createRandomAnimalWithDefaultLookingForHumanState();
        createRandomAnimalWithDefaultLookingForHumanState();

        webTestClient.get()
                     .uri(GET_ANIMALS_URL + "?page=0&size=2")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(PagedEntity.class)
                     .consumeWith(animalsEntityExchangeResult -> {
                         var pagedEntity = animalsEntityExchangeResult.getResponseBody();
                         assertNotNull(pagedEntity);
                         assertThat(pagedEntity.isEmpty(), is(false));
                         assertThat(pagedEntity.getNumberOfElements(), is(2));
                         assertThat(pagedEntity.getSize(), is(2));
                         assertThat(pagedEntity.isFirst(), is(true));
                         assertThat(pagedEntity.getContent().size(), is(2));
                     });
    }
}