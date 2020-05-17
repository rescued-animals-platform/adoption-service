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

package ec.animal.adoption.domain.animal.dto;

import ec.animal.adoption.domain.animal.EstimatedAge;
import ec.animal.adoption.domain.animal.Sex;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import ec.animal.adoption.domain.state.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ec.animal.adoption.TestUtils.getRandomEstimatedAge;
import static ec.animal.adoption.TestUtils.getRandomSex;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.domain.state.StateName.LOOKING_FOR_HUMAN;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateAnimalDtoTest {

    private String clinicalRecord;
    private String name;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private State adoptedState;
    private Organization organization;

    @BeforeEach
    void setUp() {
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        species = getRandomSpecies();
        estimatedAge = getRandomEstimatedAge();
        sex = getRandomSex();
        adoptedState = State.adopted(randomAlphabetic(10));
        organization = OrganizationFactory.random().build();
    }

    @Test
    void shouldCreateACreateAnimalDto() {
        CreateAnimalDto createAnimalDto = new CreateAnimalDto(clinicalRecord,
                                                              name,
                                                              species,
                                                              estimatedAge,
                                                              sex,
                                                              adoptedState,
                                                              organization);

        assertAll(() -> assertEquals(clinicalRecord, createAnimalDto.getClinicalRecord()),
                  () -> assertEquals(name, createAnimalDto.getName()),
                  () -> assertEquals(species, createAnimalDto.getSpecies()),
                  () -> assertEquals(estimatedAge, createAnimalDto.getEstimatedAge()),
                  () -> assertEquals(sex, createAnimalDto.getSex()),
                  () -> assertEquals(adoptedState.getName().name(), createAnimalDto.getStateNameAsString()),
                  () -> assertEquals(organization, createAnimalDto.getOrganization()),
                  () -> assertEquals(organization.getOrganizationId(), createAnimalDto.getOrganizationId()));

    }

    @Test
    void shouldSetLookingForHumanAsDefaultWhenCreateStateDtoIsNull() {
        CreateAnimalDto createAnimalDto = new CreateAnimalDto(clinicalRecord,
                                                              name,
                                                              species,
                                                              estimatedAge,
                                                              sex,
                                                              null,
                                                              organization);

        assertEquals(LOOKING_FOR_HUMAN.name(), createAnimalDto.getStateNameAsString());
    }

    @Test
    void shouldReturnAdoptionFormIdFromStateWhenPresent() {
        String adoptionFormId = randomAlphabetic(10);
        State state = State.adopted(adoptionFormId);
        CreateAnimalDto createAnimalDto = CreateAnimalDtoFactory.random().withState(state).build();

        assertTrue(createAnimalDto.getAdoptionFormId().isPresent());
        assertEquals(adoptionFormId, createAnimalDto.getAdoptionFormId().get());
    }

    @Test
    void shouldReturnEmptyWhenAdoptionFormIdIsNotPresentInState() {
        State state = State.lookingForHuman();
        CreateAnimalDto createAnimalDto = CreateAnimalDtoFactory.random().withState(state).build();

        assertTrue(createAnimalDto.getAdoptionFormId().isEmpty());
    }

    @Test
    void shouldReturnNotesFromStateWhenPresent() {
        String notes = randomAlphabetic(10);
        State state = State.unavailable(notes);
        CreateAnimalDto createAnimalDto = CreateAnimalDtoFactory.random().withState(state).build();

        assertTrue(createAnimalDto.getNotes().isPresent());
        assertEquals(notes, createAnimalDto.getNotes().get());
    }

    @Test
    void shouldReturnEmptyWhenNotesIsNotPresentInState() {
        State state = State.adopted(randomAlphabetic(10));
        CreateAnimalDto createAnimalDto = CreateAnimalDtoFactory.random().withState(state).build();

        assertTrue(createAnimalDto.getNotes().isEmpty());
    }
}