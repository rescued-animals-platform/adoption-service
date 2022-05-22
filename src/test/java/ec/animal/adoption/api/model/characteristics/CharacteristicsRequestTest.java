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

package ec.animal.adoption.api.model.characteristics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.api.model.characteristics.temperaments.TemperamentsRequestBuilder;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.TemperamentsFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static ec.animal.adoption.TestUtils.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CharacteristicsRequestTest {

    private static final String SIZE_IS_REQUIRED = "Size is required";
    private static final String PHYSICAL_ACTIVITY_IS_REQUIRED = "Physical activity is required";
    private static final String AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED = "At least one temperament is required";
    private static final String SIZE = "size";
    private static final String PHYSICAL_ACTIVITY = "physicalActivity";
    private static final String TEMPERAMENTS = "temperaments";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @Test
    void shouldDeSerializeAndReturnCharacteristicsWithEmptySetOfFriendlyWith() throws JSONException, JsonProcessingException {
        Size size = getRandomSize();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        String characteristicsRequestAsJson = new JSONObject()
                .put(SIZE, size.name())
                .put(PHYSICAL_ACTIVITY, physicalActivity.name())
                .put(TEMPERAMENTS, new JSONObject().put("sociability", "SHY"))
                .toString();

        CharacteristicsRequest characteristicsRequest = objectMapper.readValue(characteristicsRequestAsJson,
                                                                               CharacteristicsRequest.class);
        Characteristics characteristics = characteristicsRequest.toDomain();

        assertAll(() -> assertEquals(size, characteristics.getSize()),
                  () -> assertEquals(physicalActivity, characteristics.getPhysicalActivity()),
                  () -> assertEquals(TemperamentsFactory.empty().withSociability(Sociability.SHY).build(),
                                     characteristics.getTemperaments()),
                  () -> assertTrue(characteristics.getFriendlyWith().isEmpty()),
                  () -> assertNull(characteristics.getIdentifier()),
                  () -> assertNull(characteristics.getRegistrationDate()));
    }

    @Test
    void shouldDeSerializeAndReturnCharacteristicsWithFriendlyWith() throws JSONException, JsonProcessingException {
        Size size = getRandomSize();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        String characteristicsRequestAsJson = new JSONObject()
                .put(SIZE, size.name())
                .put(PHYSICAL_ACTIVITY, physicalActivity.name())
                .put(TEMPERAMENTS, new JSONObject().put("balance", "BALANCED"))
                .put("friendlyWith", new JSONArray().put("DOGS").put("CHILDREN"))
                .toString();

        CharacteristicsRequest characteristicsRequest = objectMapper.readValue(characteristicsRequestAsJson,
                                                                               CharacteristicsRequest.class);
        Characteristics characteristics = characteristicsRequest.toDomain();

        assertAll(() -> assertEquals(size, characteristics.getSize()),
                  () -> assertEquals(physicalActivity, characteristics.getPhysicalActivity()),
                  () -> assertEquals(TemperamentsFactory.empty().withBalance(Balance.BALANCED).build(),
                                     characteristics.getTemperaments()),
                  () -> assertEquals(Sets.newHashSet(FriendlyWith.DOGS, FriendlyWith.CHILDREN),
                                     characteristics.getFriendlyWith()),
                  () -> assertNull(characteristics.getIdentifier()),
                  () -> assertNull(characteristics.getRegistrationDate()));
    }

    @Test
    void shouldDeSerializeAndReturnCharacteristicsWithFriendlyWithContainingNoDuplicates() throws JSONException, JsonProcessingException {
        Size size = getRandomSize();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        String characteristicsRequestAsJson = new JSONObject()
                .put(SIZE, size.name())
                .put(PHYSICAL_ACTIVITY, physicalActivity.name())
                .put(TEMPERAMENTS, new JSONObject().put("docility", "DOCILE"))
                .put("friendlyWith", new JSONArray().put("DOGS").put("DOGS").put("CATS"))
                .toString();

        CharacteristicsRequest characteristicsRequest = objectMapper.readValue(characteristicsRequestAsJson,
                                                                               CharacteristicsRequest.class);
        Characteristics characteristics = characteristicsRequest.toDomain();

        assertAll(() -> assertEquals(size, characteristics.getSize()),
                  () -> assertEquals(physicalActivity, characteristics.getPhysicalActivity()),
                  () -> assertEquals(TemperamentsFactory.empty().withDocility(Docility.DOCILE).build(),
                                     characteristics.getTemperaments()),
                  () -> assertEquals(2, characteristics.getFriendlyWith().size()),
                  () -> assertEquals(Sets.newHashSet(FriendlyWith.DOGS, FriendlyWith.CATS),
                                     characteristics.getFriendlyWith()),
                  () -> assertNull(characteristics.getIdentifier()),
                  () -> assertNull(characteristics.getRegistrationDate()));
    }

    @Test
    void shouldValidateNonNullSize() {
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(
                null, getRandomPhysicalActivity(), TemperamentsRequestBuilder.random().build()
        );

        Set<ConstraintViolation<CharacteristicsRequest>> constraintViolations = getValidator().validate(characteristicsRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CharacteristicsRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(SIZE_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(SIZE));
    }

    @Test
    void shouldValidateNonNullPhysicalActivity() {
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(
                getRandomSize(), null, TemperamentsRequestBuilder.random().build()
        );

        Set<ConstraintViolation<CharacteristicsRequest>> constraintViolations = getValidator().validate(characteristicsRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CharacteristicsRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(PHYSICAL_ACTIVITY_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(PHYSICAL_ACTIVITY));
    }

    @Test
    void shouldValidateNonNullTemperaments() {
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(
                getRandomSize(), getRandomPhysicalActivity(), null
        );

        Set<ConstraintViolation<CharacteristicsRequest>> constraintViolations = getValidator().validate(characteristicsRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CharacteristicsRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(TEMPERAMENTS));
    }

    @Test
    void shouldValidateNonEmptyTemperaments() {
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(
                getRandomSize(), getRandomPhysicalActivity(), TemperamentsRequestBuilder.empty().build()
        );

        Set<ConstraintViolation<CharacteristicsRequest>> constraintViolations = getValidator().validate(characteristicsRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<CharacteristicsRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is(TEMPERAMENTS));
    }
}