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
import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.models.rest.ApiError;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class AnimalResourceIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void shouldReturn201Created() {
        Animal animal = AnimalBuilder.random().build();

        ResponseEntity<Animal> response = testClient.postForEntity(
                ANIMALS_URL, animal, Animal.class, getHttpHeaders()
        );

        assertCreated(response, animal);
    }

    @Test
    public void shouldReturn201CreatedSettingLookingForHumanAsDefaultStateWhenStateIsNotSend() {
        Animal animal = AnimalBuilder.random().withState(null).build();
        String animalAsJson = "{\"clinicalRecord\":\"" + animal.getClinicalRecord() +
                "\",\"name\":\"" + animal.getName() + "\",\"registrationDate\":\"" + animal.getRegistrationDate() +
                "\",\"species\":\"" + animal.getSpecies() + "\",\"estimatedAge\":\"" +
                animal.getEstimatedAge() + "\",\"sex\":\"" + animal.getSex() + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(animalAsJson, getHttpHeaders());

        ResponseEntity<Animal> response = testClient.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, Animal.class
        );

        assertCreated(response, animal);
    }

    @Test
    public void shouldReturn400BadRequestWhenJsonCannotBeParsed() {
        Animal animal = AnimalBuilder.random().build();
        String wrongRegistrationDate = randomAlphabetic(10);
        String animalWithWrongData = "{\"clinicalRecord\":\"" + animal.getClinicalRecord() +
                "\",\"name\":\"" + animal.getName() + "\",\"registrationDate\":\"" + wrongRegistrationDate +
                "\",\"species\":\"" + animal.getSpecies() + "\",\"estimatedAge\":\"" + animal.getEstimatedAge() +
                "\",\"sex\":\"12345\"}";
        HttpEntity<String> entity = new HttpEntity<>(animalWithWrongData, getHttpHeaders());

        ResponseEntity<ApiError> response = testClient.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, ApiError.class
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn400BadRequestWhenMissingDataIsProvided() {
        Animal animal = AnimalBuilder.random().build();
        String animalWithMissingData = "{\"clinicalRecord\":\"\",\"name\":\"" + animal.getName() +
                "\",\"species\":\"" + animal.getSpecies() + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(animalWithMissingData, getHttpHeaders());

        ResponseEntity<ApiError> response = testClient.exchange(
                ANIMALS_URL, HttpMethod.POST, entity, ApiError.class
        );

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        ApiError apiError = response.getBody();
        assertNotNull(apiError);
    }

    @Test
    public void shouldReturn409ConflictWhenCreatingAnAnimalThatAlreadyExists() {
        Animal animal = AnimalBuilder.random().build();
        testClient.postForEntity(ANIMALS_URL, animal, Animal.class, getHttpHeaders());

        ResponseEntity<ApiError> conflictResponse = testClient.postForEntity(
                ANIMALS_URL, animal, ApiError.class, getHttpHeaders()
        );

        assertThat(conflictResponse.getStatusCode(), is(CONFLICT));
        ApiError apiError = conflictResponse.getBody();
        assertNotNull(apiError);
    }

    private void assertCreated(ResponseEntity<Animal> response, Animal expectedAnimal) {
        assertThat(response.getStatusCode(), is(CREATED));
        Animal animal = response.getBody();
        assertNotNull(animal);
        assertNotNull(animal.getUuid());
        assertThat(animal.getClinicalRecord(), is(expectedAnimal.getClinicalRecord()));
        assertThat(animal.getName(), is(expectedAnimal.getName()));
        assertThat(animal.getSpecies(), is(expectedAnimal.getSpecies()));
        assertThat(animal.getEstimatedAge(), is(expectedAnimal.getEstimatedAge()));
        assertReflectionEquals(animal.getState(), expectedAnimal.getState());
    }
}