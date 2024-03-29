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

package ec.animal.adoption.adapter.rest.model.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ApiErrorResponseTest {

    private ApiErrorResponse apiErrorResponse;

    @BeforeEach
    public void setUp() {
        String debugMessage = randomAlphabetic(10);
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        String message = randomAlphabetic(10);
        List<ValidationApiSubErrorResponse> subErrors = new ArrayList<>();
        subErrors.add(new ValidationApiSubErrorResponse(randomAlphabetic(10), randomAlphabetic(10)));
        subErrors.add(new ValidationApiSubErrorResponse(randomAlphabetic(10), randomAlphabetic(10)));
        apiErrorResponse = new ApiErrorResponse(status, message, debugMessage);
        apiErrorResponse.setSubErrors(subErrors);
    }

    @Test
    void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(ApiErrorResponse.class)
                      .withIgnoredFields("timestamp")
                      .suppress(Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE)
                      .verify();
    }

    @Test
    void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        String serializedApiError = objectMapper.writeValueAsString(apiErrorResponse);

        ApiErrorResponse deserializedApiErrorResponse = objectMapper.readValue(
                serializedApiError, ApiErrorResponse.class
        );

        assertThat(deserializedApiErrorResponse, is(apiErrorResponse));
    }
}