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

package ec.animal.adoption.api.model.animal;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.api.model.state.StateRequest;
import ec.animal.adoption.domain.animal.EstimatedAge;
import ec.animal.adoption.domain.animal.Sex;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.animal.dto.CreateAnimalDto;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import ec.animal.adoption.domain.state.State;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Set;

import static ec.animal.adoption.TestUtils.getRandomEstimatedAge;
import static ec.animal.adoption.TestUtils.getRandomSex;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getValidator;
import static ec.animal.adoption.domain.state.StateName.ADOPTED;
import static ec.animal.adoption.domain.state.StateName.LOOKING_FOR_HUMAN;
import static ec.animal.adoption.domain.state.StateName.UNAVAILABLE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateAnimalRequestTest {

    private static final String ANIMAL_NAME_MUST_BE_A_NON_EMPTY_STRING = "Animal name must be a non-empty string";
    private static final String ANIMAL_CLINICAL_RECORD_IS_REQUIRED = "Animal clinical record is required";
    private static final String ANIMAL_SPECIES_IS_REQUIRED = "Animal species is required";
    private static final String ANIMAL_ESTIMATED_AGE_IS_REQUIRED = "Animal estimated age is required";
    private static final String ANIMAL_SEX_IS_REQUIRED = "Animal sex is required";
    private static final String STATE_NAME_IS_REQUIRED = "State name is required";
    private static final String CLINICAL_RECORD = "clinicalRecord";
    private static final String NAME = "name";
    private static final String SPECIES = "species";
    private static final String ESTIMATED_AGE = "estimatedAge";
    private static final String SEX = "sex";
    private static final String STATE = "state";

    private ObjectMapper objectMapper;
    private String clinicalRecord;
    private String name;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        species = getRandomSpecies();
        estimatedAge = getRandomEstimatedAge();
        sex = getRandomSex();
    }

    @Test
    void shouldBuildCreateAnimalDtoFromCreateAnimalRequestWhenCreateStateRequestIsNull() {
        CreateAnimalRequest createAnimalRequest = new CreateAnimalRequest(clinicalRecord,
                                                                          name,
                                                                          species,
                                                                          estimatedAge,
                                                                          sex,
                                                                          null);
        Organization organization = OrganizationFactory.random().build();

        CreateAnimalDto createAnimalDto = createAnimalRequest.toDomainWith(organization);

        assertAll(() -> assertEquals(clinicalRecord, createAnimalDto.getClinicalRecord()),
                  () -> assertEquals(name, createAnimalDto.getName()),
                  () -> assertEquals(species, createAnimalDto.getSpecies()),
                  () -> assertEquals(estimatedAge, createAnimalDto.getEstimatedAge()),
                  () -> assertEquals(sex, createAnimalDto.getSex()),
                  () -> assertEquals(organization, createAnimalDto.getOrganization()),
                  () -> assertEquals(LOOKING_FOR_HUMAN.name(), createAnimalDto.getStateNameAsString())
        );
    }

    @Test
    void shouldBuildCreateAnimalDtoFromCreateAnimalRequestWhenCreateStateRequestIsNotNull() {
        String adoptionFormId = randomAlphabetic(10);
        StateRequest stateRequest = new StateRequest(ADOPTED, adoptionFormId, null);
        CreateAnimalRequest createAnimalRequest = new CreateAnimalRequest(clinicalRecord,
                                                                          name,
                                                                          species,
                                                                          estimatedAge,
                                                                          sex,
                                                                          stateRequest);
        Organization organization = OrganizationFactory.random().build();

        CreateAnimalDto createAnimalDto = createAnimalRequest.toDomainWith(organization);

        assertAll(() -> assertEquals(clinicalRecord, createAnimalDto.getClinicalRecord()),
                  () -> assertEquals(name, createAnimalDto.getName()),
                  () -> assertEquals(species, createAnimalDto.getSpecies()),
                  () -> assertEquals(estimatedAge, createAnimalDto.getEstimatedAge()),
                  () -> assertEquals(sex, createAnimalDto.getSex()),
                  () -> assertEquals(organization, createAnimalDto.getOrganization()),
                  () -> assertEquals(ADOPTED.name(), createAnimalDto.getStateNameAsString()),
                  () -> assertTrue(createAnimalDto.getAdoptionFormId().isPresent()),
                  () -> assertEquals(adoptionFormId, createAnimalDto.getAdoptionFormId().get())
        );
    }

    @Test
    public void shouldValidateNonNullClinicalRecord() {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random().withClinicalRecord(null).build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CreateAnimalRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_CLINICAL_RECORD_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(CLINICAL_RECORD));
    }

    @Test
    public void shouldValidateNonEmptyClinicalRecord() {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random().withClinicalRecord("").build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CreateAnimalRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_CLINICAL_RECORD_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(CLINICAL_RECORD));
    }

    @Test
    public void shouldAllowNullName() {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random().withName(null).build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(0));
    }

    @Test
    public void shouldValidateNonEmptyName() {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random().withName(" ").build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CreateAnimalRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_NAME_MUST_BE_A_NON_EMPTY_STRING));
        assertThat(constraintViolation.getPropertyPath().toString(), is(NAME));
    }

    @Test
    public void shouldValidateNonNullSpecies() {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random().withSpecies(null).build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CreateAnimalRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_SPECIES_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(SPECIES));
    }

    @Test
    public void shouldValidateNonNullEstimatedAge() {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random().withEstimatedAge(null).build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CreateAnimalRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_ESTIMATED_AGE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(ESTIMATED_AGE));
    }

    @Test
    public void shouldValidateNonNullSex() {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random().withSex(null).build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CreateAnimalRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(ANIMAL_SEX_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(SEX));
    }

    @Test
    public void shouldAllowNullStateRequest() {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random().withState(null).build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(0));
    }

    @Test
    public void shouldValidateStateRequestWhenPresent() {
        StateRequest stateRequest = new StateRequest(null, null, null);
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random()
                                                                            .withCreateStateRequest(stateRequest)
                                                                            .build();

        Set<ConstraintViolation<CreateAnimalRequest>> constraintViolations = getValidator().validate(createAnimalRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CreateAnimalRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(STATE_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("state.name"));
    }

    @Test
    public void shouldDeserializeCreateAnimalRequestWithNoStateRequest() throws IOException, JSONException {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random()
                                                                            .withClinicalRecord(clinicalRecord)
                                                                            .withName(name)
                                                                            .withSpecies(species)
                                                                            .withEstimatedAge(estimatedAge)
                                                                            .withSex(sex)
                                                                            .withState(null)
                                                                            .build();
        String createAnimalRequestAsJson = new JSONObject().put(CLINICAL_RECORD, clinicalRecord)
                                                           .put(NAME, name)
                                                           .put(SPECIES, species.name())
                                                           .put(ESTIMATED_AGE, estimatedAge.name())
                                                           .put(SEX, sex.name())
                                                           .toString();

        CreateAnimalRequest deSerializedCreateAnimalRequest = objectMapper.readValue(createAnimalRequestAsJson,
                                                                                     CreateAnimalRequest.class);

        Assertions.assertThat(deSerializedCreateAnimalRequest).usingRecursiveComparison().isEqualTo(createAnimalRequest);
    }

    @Test
    public void shouldDeserializeCreateAnimalRequestWithLookingForHumanStateRequest() throws IOException, JSONException {
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random()
                                                                            .withClinicalRecord(clinicalRecord)
                                                                            .withName(name)
                                                                            .withSpecies(species)
                                                                            .withEstimatedAge(estimatedAge)
                                                                            .withSex(sex)
                                                                            .withState(State.lookingForHuman())
                                                                            .build();
        JSONObject createStateRequestAsJson = new JSONObject().put(NAME, LOOKING_FOR_HUMAN.name());
        String createAnimalRequestAsJson = new JSONObject().put(CLINICAL_RECORD, clinicalRecord)
                                                           .put(NAME, name)
                                                           .put(SPECIES, species.name())
                                                           .put(ESTIMATED_AGE, estimatedAge.name())
                                                           .put(SEX, sex.name())
                                                           .put(STATE, createStateRequestAsJson)
                                                           .toString();

        CreateAnimalRequest deSerializedCreateAnimalRequest = objectMapper.readValue(createAnimalRequestAsJson,
                                                                                     CreateAnimalRequest.class);

        Assertions.assertThat(deSerializedCreateAnimalRequest).usingRecursiveComparison().isEqualTo(createAnimalRequest);
    }

    @Test
    public void shouldDeserializeCreateAnimalRequestWithAdoptedStateRequest() throws IOException, JSONException {
        String adoptionFormId = randomAlphabetic(10);
        State adoptedState = State.adopted(adoptionFormId);
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random()
                                                                            .withClinicalRecord(clinicalRecord)
                                                                            .withName(name)
                                                                            .withSpecies(species)
                                                                            .withEstimatedAge(estimatedAge)
                                                                            .withSex(sex)
                                                                            .withState(adoptedState)
                                                                            .build();
        JSONObject createStateRequestAsJson = new JSONObject().put(NAME, ADOPTED.name())
                                                              .put("adoptionFormId", adoptionFormId);
        String createAnimalRequestAsJson = new JSONObject().put(CLINICAL_RECORD, clinicalRecord)
                                                           .put(NAME, name)
                                                           .put(SPECIES, species.name())
                                                           .put(ESTIMATED_AGE, estimatedAge.name())
                                                           .put(SEX, sex.name())
                                                           .put(STATE, createStateRequestAsJson)
                                                           .toString();

        CreateAnimalRequest deSerializedCreateAnimalRequest = objectMapper.readValue(createAnimalRequestAsJson,
                                                                                     CreateAnimalRequest.class);

        Assertions.assertThat(deSerializedCreateAnimalRequest).usingRecursiveComparison().isEqualTo(createAnimalRequest);
    }

    @Test
    public void shouldDeserializeCreateAnimalRequestWithUnavailableStateRequest() throws IOException, JSONException {
        String notes = randomAlphabetic(50);
        State unavailableState = State.unavailable(notes);
        CreateAnimalRequest createAnimalRequest = CreateAnimalRequestBuilder.random()
                                                                            .withClinicalRecord(clinicalRecord)
                                                                            .withName(name)
                                                                            .withSpecies(species)
                                                                            .withEstimatedAge(estimatedAge)
                                                                            .withSex(sex)
                                                                            .withState(unavailableState)
                                                                            .build();
        JSONObject createStateRequestAsJson = new JSONObject().put(NAME, UNAVAILABLE.name())
                                                              .put("notes", notes);
        String createAnimalRequestAsJson = new JSONObject().put(CLINICAL_RECORD, clinicalRecord)
                                                           .put(NAME, name)
                                                           .put(SPECIES, species.name())
                                                           .put(ESTIMATED_AGE, estimatedAge.name())
                                                           .put(SEX, sex.name())
                                                           .put(STATE, createStateRequestAsJson)
                                                           .toString();

        CreateAnimalRequest deSerializedCreateAnimalRequest = objectMapper.readValue(createAnimalRequestAsJson,
                                                                                     CreateAnimalRequest.class);

        Assertions.assertThat(deSerializedCreateAnimalRequest).usingRecursiveComparison().isEqualTo(createAnimalRequest);
    }
}