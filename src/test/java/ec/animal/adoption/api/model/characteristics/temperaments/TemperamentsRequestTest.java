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

package ec.animal.adoption.api.model.characteristics.temperaments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.animal.characteristics.temperaments.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Stream;

import static ec.animal.adoption.TestUtils.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TemperamentsRequestTest {

    private static final String AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED = "At least one temperament is required";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = TestUtils.getObjectMapper();
    }

    @ParameterizedTest(name = "{index} deserialize valid temperaments request and returns temperaments domain object {0}")
    @MethodSource("validTemperaments")
    void shouldDeSerializeAndReturnTemperaments(final Temperaments expectedTemperaments) throws JSONException, JsonProcessingException {
        String temperamentsRequestAsJson = new JSONObject()
                .put("sociability", expectedTemperaments.getSociability().orElse(null))
                .put("docility", expectedTemperaments.getDocility().orElse(null))
                .put("balance", expectedTemperaments.getBalance().orElse(null))
                .toString();

        TemperamentsRequest temperamentsRequest = objectMapper.readValue(temperamentsRequestAsJson,
                                                                         TemperamentsRequest.class);
        Temperaments temperaments = temperamentsRequest.toDomain();

        assertEquals(expectedTemperaments, temperaments);
    }

    private static Stream<Arguments> validTemperaments() {
        return Stream.of(
                Arguments.of(TemperamentsFactory.empty().withSociability(Sociability.SOCIABLE).build()),
                Arguments.of(TemperamentsFactory.empty().withBalance(Balance.NEITHER_BALANCED_NOR_POSSESSIVE).build()),
                Arguments.of(TemperamentsFactory.empty().withDocility(Docility.VERY_DOMINANT).build()),
                Arguments.of(TemperamentsFactory.random().build())
        );
    }

    @Test
    void shouldReturnTrueIfTemperamentsRequestIsEmpty() {
        TemperamentsRequest temperamentsRequest = TemperamentsRequestBuilder.empty().build();

        assertThat(temperamentsRequest.isEmpty(), is(true));
    }

    @Test
    void shouldReturnFalseIfOnlySociabilityIsSet() {
        TemperamentsRequest temperamentsRequest = TemperamentsRequestBuilder.empty()
                                                                            .withSociability(getRandomSociability())
                                                                            .build();

        assertThat(temperamentsRequest.isEmpty(), is(false));
    }

    @Test
    void shouldReturnFalseIfOnlyDocilityIsSet() {
        TemperamentsRequest temperamentsRequest = TemperamentsRequestBuilder.empty()
                                                                            .withDocility(getRandomDocility())
                                                                            .build();

        assertThat(temperamentsRequest.isEmpty(), is(false));
    }

    @Test
    void shouldReturnFalseIfOnlyBalanceIsSet() {
        TemperamentsRequest temperamentsRequest = TemperamentsRequestBuilder.empty()
                                                                            .withBalance(getRandomBalance())
                                                                            .build();

        assertThat(temperamentsRequest.isEmpty(), is(false));
    }

    @Test
    void shouldReturnFalseIfSociabilityAndDocilityAreSet() {
        TemperamentsRequest temperamentsRequest = TemperamentsRequestBuilder.random().withBalance(null).build();

        assertThat(temperamentsRequest.isEmpty(), is(false));
    }

    @Test
    void shouldReturnFalseIfSociabilityAndBalanceAreSet() {
        TemperamentsRequest temperamentsRequest = TemperamentsRequestBuilder.random().withDocility(null).build();

        assertThat(temperamentsRequest.isEmpty(), is(false));
    }

    @Test
    void shouldReturnFalseIfDocilityAndBalanceAreSet() {
        TemperamentsRequest temperamentsRequest = TemperamentsRequestBuilder.random().withSociability(null).build();

        assertThat(temperamentsRequest.isEmpty(), is(false));
    }

    @Test
    void shouldValidateNonEmptyTemperamentsRequest() {
        TemperamentsRequest temperamentsRequest = TemperamentsRequestBuilder.empty().build();

        Set<ConstraintViolation<TemperamentsRequest>> constraintViolations = getValidator().validate(temperamentsRequest);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<TemperamentsRequest> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(AT_LEAST_ONE_TEMPERAMENT_IS_REQUIRED));
    }
}