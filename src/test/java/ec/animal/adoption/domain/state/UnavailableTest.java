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

package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static ec.animal.adoption.TestUtils.getValidator;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UnavailableTest {

    private static final String NOTES_ARE_REQUIRED_FOR_UNAVAILABLE_STATE = "Notes are required for unavailable state";

    private final ObjectMapper objectMapper = TestUtils.getObjectMapper();

    @Test
    public void shouldBeAnInstanceOfState() {
        Unavailable unavailableState = new Unavailable(now(), randomAlphabetic(20));

        assertThat(unavailableState, is(instanceOf(State.class)));
    }

    @Test
    void shouldReturnUnavailableName() {
        State unavailableState = new Unavailable(now(), randomAlphabetic(20));

        assertEquals(unavailableState.getName(), "unavailable");
    }

    @Test
    public void shouldBeSerializable() throws JsonProcessingException {
        String notes = randomAlphabetic(20);
        LocalDateTime localDateTime = now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        String expectedSerializedUnavailableState = "{\"name\":\"unavailable\",\"date\":" + serializedLocalDateTime +
                ",\"notes\":\"" + notes + "\"}";
        Unavailable unavailableState = new Unavailable(localDateTime, notes);

        String serializedUnavailableState = objectMapper.writeValueAsString(unavailableState);

        assertThat(serializedUnavailableState, is(expectedSerializedUnavailableState));
    }

    @Test
    public void shouldBeDeserializableWithDateAndNotes() throws IOException {
        String notes = randomAlphabetic(20);
        LocalDateTime localDateTime = now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        String serializedUnavailableState = "{\"name\":\"unavailable\",\"date\":" + serializedLocalDateTime +
                ",\"notes\":\"" + notes + "\"}";

        Unavailable deserializedUnavailableState = objectMapper.readValue(
                serializedUnavailableState, Unavailable.class
        );

        assertNotNull(deserializedUnavailableState);
        assertThat(deserializedUnavailableState.getDate(), is(localDateTime));
        assertThat(deserializedUnavailableState.getNotes(), is(notes));
    }

    @Test
    public void shouldBeDeserializableWithNotesOnly() throws IOException {
        String notes = randomAlphabetic(20);
        String serializedUnavailableState = "{\"name\":\"unavailable\",\"notes\":\"" + notes + "\"}";

        Unavailable deserializedUnavailableState = objectMapper.readValue(
                serializedUnavailableState, Unavailable.class
        );

        assertNotNull(deserializedUnavailableState);
        assertNull(deserializedUnavailableState.getDate());
        assertThat(deserializedUnavailableState.getNotes(), is(notes));
    }

    @Test
    public void shouldValidateNonNullNotes() {
        Unavailable unavailable = new Unavailable(now(), null);

        Set<ConstraintViolation<Unavailable>> constraintViolations = getValidator().validate(unavailable);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Unavailable> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(NOTES_ARE_REQUIRED_FOR_UNAVAILABLE_STATE));
        assertThat(constraintViolation.getPropertyPath().toString(), is("notes"));
    }

    @Test
    public void shouldValidateNonEmptyNotes() {
        Unavailable unavailable = new Unavailable(now(), "");

        Set<ConstraintViolation<Unavailable>> constraintViolations = getValidator().validate(unavailable);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Unavailable> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(NOTES_ARE_REQUIRED_FOR_UNAVAILABLE_STATE));
        assertThat(constraintViolation.getPropertyPath().toString(), is("notes"));
    }
}