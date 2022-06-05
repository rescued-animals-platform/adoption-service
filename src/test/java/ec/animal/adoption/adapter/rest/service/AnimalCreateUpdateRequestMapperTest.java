package ec.animal.adoption.adapter.rest.service;

import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateRequest;
import ec.animal.adoption.adapter.rest.model.state.StateRequest;
import ec.animal.adoption.domain.model.animal.EstimatedAge;
import ec.animal.adoption.domain.model.animal.Sex;
import ec.animal.adoption.domain.model.animal.Species;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.organization.OrganizationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ec.animal.adoption.TestUtils.getRandomEstimatedAge;
import static ec.animal.adoption.TestUtils.getRandomSex;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.domain.model.state.StateName.ADOPTED;
import static ec.animal.adoption.domain.model.state.StateName.LOOKING_FOR_HUMAN;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalCreateUpdateRequestMapperTest {

    private String clinicalRecord;
    private String name;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;

    @BeforeEach
    void setUp() {
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        species = getRandomSpecies();
        estimatedAge = getRandomEstimatedAge();
        sex = getRandomSex();
    }

    @Test
    void shouldBuildAnimalDtoFromCreateAnimalRequestWhenStateRequestIsNull() {
        AnimalCreateUpdateRequest animalCreateUpdateRequest = new AnimalCreateUpdateRequest(clinicalRecord,
                                                                                            name,
                                                                                            species,
                                                                                            estimatedAge,
                                                                                            sex,
                                                                                            null);
        Organization organization = OrganizationFactory.random().build();

        AnimalDto animalDto = AnimalCreateUpdateRequestMapper.MAPPER.toAnimalDto(animalCreateUpdateRequest, organization);

        assertAll(() -> assertEquals(clinicalRecord, animalDto.clinicalRecord()),
                  () -> assertEquals(name, animalDto.name()),
                  () -> assertEquals(species, animalDto.species()),
                  () -> assertEquals(estimatedAge, animalDto.estimatedAge()),
                  () -> assertEquals(sex, animalDto.sex()),
                  () -> assertEquals(organization, animalDto.organization()),
                  () -> assertEquals(LOOKING_FOR_HUMAN.name(), animalDto.getStateNameAsString()));
    }

    @Test
    void shouldBuildAnimalDtoFromCreateAnimalRequestWhenStateRequestIsNotNull() {
        String adoptionFormId = randomAlphabetic(10);
        StateRequest stateRequest = new StateRequest(ADOPTED, adoptionFormId, null);
        AnimalCreateUpdateRequest animalCreateUpdateRequest = new AnimalCreateUpdateRequest(clinicalRecord,
                                                                                            name,
                                                                                            species,
                                                                                            estimatedAge,
                                                                                            sex,
                                                                                            stateRequest);
        Organization organization = OrganizationFactory.random().build();

        AnimalDto animalDto = AnimalCreateUpdateRequestMapper.MAPPER.toAnimalDto(animalCreateUpdateRequest, organization);

        assertAll(() -> assertEquals(clinicalRecord, animalDto.clinicalRecord()),
                  () -> assertEquals(name, animalDto.name()),
                  () -> assertEquals(species, animalDto.species()),
                  () -> assertEquals(estimatedAge, animalDto.estimatedAge()),
                  () -> assertEquals(sex, animalDto.sex()),
                  () -> assertEquals(organization, animalDto.organization()),
                  () -> assertEquals(ADOPTED.name(), animalDto.getStateNameAsString()),
                  () -> {
                      assertTrue(animalDto.getAdoptionFormId().isPresent());
                      assertEquals(adoptionFormId, animalDto.getAdoptionFormId().get());
                  });
    }

    @Test
    void shouldReturnNullAnimalDtoIfBothCreateUpdateRequestAndOrganizationAreNull() {
        assertNull(AnimalCreateUpdateRequestMapper.MAPPER.toAnimalDto(null, null));
    }
}