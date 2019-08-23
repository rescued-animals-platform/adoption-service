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
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.error.ApiError;
import ec.animal.adoption.domain.state.LookingForHuman;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert", "PMD.TooManyMethods"})
public class AnimalResourceIntegrationTest extends AbstractResourceIntegrationTest {

    @Test
    public void shouldReturn201Created() {
        Animal animal = AnimalBuilder.random().build();

        webTestClient.post()
                .uri(ANIMALS_URL)
                .syncBody(animal)
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
                    assertThat(createdAnimal.getState(), is(instanceOf(animal.getState().getClass())));
                });
    }

    @Test
    public void shouldReturn201CreatedSettingLookingForHumanAsDefaultStateWhenStateIsNotSent() {
        Animal animal = AnimalBuilder.random().withState(null).build();

        webTestClient.post()
                .uri(ANIMALS_URL)
                .syncBody(animal)
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

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        Animal animal = AnimalBuilder.random().build();
        String wrongRegistrationDate = randomAlphabetic(10);
        String animalWithWrongData = "{\"clinicalRecord\":\"" + animal.getClinicalRecord() +
                "\",\"name\":\"" + animal.getName() + "\",\"registrationDate\":\"" + wrongRegistrationDate +
                "\",\"species\":\"" + animal.getSpecies() + "\",\"estimatedAge\":\"" + animal.getEstimatedAge() +
                "\",\"sex\":\"12345\"}";

        webTestClient.post()
                .uri(ANIMALS_URL)
                .syncBody(animalWithWrongData)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
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
                .uri(ANIMALS_URL)
                .syncBody(animalWithMissingData)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingAnAnimalThatAlreadyExists() {
        Animal animal = AnimalBuilder.random().build();
        webTestClient.post()
                .uri(ANIMALS_URL)
                .syncBody(animal)
                .exchange()
                .expectStatus()
                .isCreated();

        webTestClient.post()
                .uri(ANIMALS_URL)
                .syncBody(animal)
                .exchange()
                .expectStatus()
                .isEqualTo(CONFLICT)
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithAnimal() {
        Animal createdAnimal = createAndSaveAnimalWithDefaultLookingForHumanState();

        webTestClient.get()
                .uri(ANIMALS_URL + "/{uuid}", createdAnimal.getUuid())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Animal.class)
                .consumeWith(animalEntityExchangeResult -> {
                    Animal foundAnimal = animalEntityExchangeResult.getResponseBody();
                    assertReflectionEquals(createdAnimal, foundAnimal);
                });
    }

    @Test
    public void shouldReturn404NotFoundWhenAnimalDoesNotExist() {
        webTestClient.get()
                .uri(ANIMALS_URL + "/{uuid}", UUID.randomUUID())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithPagedEntityContainingFirstPageAndThreeAnimalsFilteredByState() {
        createAndSavePrimaryLinkPictureForAnimal(createAndSaveAnimalWithDefaultLookingForHumanState().getUuid());
        createAndSavePrimaryLinkPictureForAnimal(createAndSaveAnimalWithDefaultLookingForHumanState().getUuid());
        createAndSavePrimaryLinkPictureForAnimal(createAndSaveAnimalWithDefaultLookingForHumanState().getUuid());
        String stateName = new LookingForHuman(LocalDateTime.now()).getStateName();

        webTestClient.get()
                .uri(ANIMALS_URL + "?state={state}&page=0&size=3", stateName)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PagedEntity.class)
                .consumeWith(animalsEntityExchangeResult -> {
                    PagedEntity pagedEntity = animalsEntityExchangeResult.getResponseBody();
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

        webTestClient.get()
                .uri(ANIMALS_URL + "?state={state}", invalidStateName)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);
    }

    @Test
    public void shouldReturn200OkWithPagedEntityContainingFirstPageAndTwoAnimals() {
        createAndSaveAnimalWithDefaultLookingForHumanState();
        createAndSaveAnimalWithDefaultLookingForHumanState();

        webTestClient.get()
                .uri(ANIMALS_URL + "?page=0&size=2")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PagedEntity.class)
                .consumeWith(animalsEntityExchangeResult -> {
                    PagedEntity pagedEntity = animalsEntityExchangeResult.getResponseBody();
                    assertNotNull(pagedEntity);
                    assertThat(pagedEntity.isEmpty(), is(false));
                    assertThat(pagedEntity.getNumberOfElements(), is(2));
                    assertThat(pagedEntity.getSize(), is(2));
                    assertThat(pagedEntity.isFirst(), is(true));
                    assertThat(pagedEntity.getContent().size(), is(2));
                });
    }
}