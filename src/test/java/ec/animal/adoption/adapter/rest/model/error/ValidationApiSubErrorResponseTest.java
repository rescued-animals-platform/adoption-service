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
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ValidationApiSubErrorResponseTest {

    @Test
    void shouldBeAnInstanceOfApiSubError() {
        ValidationApiSubErrorResponse validationApiSubError = new ValidationApiSubErrorResponse(randomAlphabetic(10), randomAlphabetic(10));

        assertThat(validationApiSubError, is(instanceOf(ApiSubErrorResponse.class)));
    }

    @Test
    void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(ValidationApiSubErrorResponse.class).usingGetClass().verify();
    }

    @Test
    void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = TestUtils.getObjectMapper();
        ValidationApiSubErrorResponse validationApiSubError = new ValidationApiSubErrorResponse(randomAlphabetic(10), randomAlphabetic(10));

        String serializedValidationError = objectMapper.writeValueAsString(validationApiSubError);
        ValidationApiSubErrorResponse deserializedValidationApiSubError = objectMapper.readValue(serializedValidationError, ValidationApiSubErrorResponse.class);

        assertThat(deserializedValidationApiSubError, is(validationApiSubError));
    }
}
