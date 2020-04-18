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
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class AdoptedTest {

    private final ObjectMapper objectMapper = TestUtils.getObjectMapper();

    @Test
    public void shouldReturnStateName() {
        assertThat(new Adopted(now(), randomAlphabetic(10)).getStateName(), is("adopted"));
    }

    @Test
    public void shouldBeAnInstanceOfState() {
        Adopted adoptedState = new Adopted(now(), randomAlphabetic(10));

        assertThat(adoptedState, is(instanceOf(State.class)));
    }

    @Test
    public void shouldBeSerializable() throws JsonProcessingException {
        String adoptionFormId = randomAlphabetic(10);
        LocalDateTime localDateTime = now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        String expectedSerializedAdoptedState = "{\"adopted\":{\"date\":" + serializedLocalDateTime +
                ",\"adoptionFormId\":\"" + adoptionFormId + "\"}}";
        Adopted adoptedState = new Adopted(localDateTime, adoptionFormId);

        String serializedAdoptedState = objectMapper.writeValueAsString(adoptedState);

        assertThat(serializedAdoptedState, is(expectedSerializedAdoptedState));
    }

    @Test
    public void shouldBeDeserializableWithoutAdoptionFormIdAndDate() throws IOException {
        String serializedAdoptedState = "{\"adopted\":{}}";

        Adopted deserializedAdoptedState = objectMapper.readValue(serializedAdoptedState, Adopted.class);

        assertNotNull(deserializedAdoptedState);
        assertNull(deserializedAdoptedState.getDate());
        assertNull(deserializedAdoptedState.getAdoptionFormId());
    }

    @Test
    public void shouldBeDeserializableWithAdoptionFormIdOnly() throws IOException {
        String adoptionFormId = randomAlphabetic(10);
        String serializedAdoptedState = "{\"adopted\":{\"adoptionFormId\":\"" + adoptionFormId + "\"}}";

        Adopted deserializedAdoptedState = objectMapper.readValue(serializedAdoptedState, Adopted.class);

        assertNotNull(deserializedAdoptedState);
        assertNull(deserializedAdoptedState.getDate());
        assertThat(deserializedAdoptedState.getAdoptionFormId(), is(adoptionFormId));
    }

    @Test
    public void shouldBeDeserializableWithDateOnly() throws IOException {
        LocalDateTime localDateTime = now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        String serializedAdoptedState = "{\"adopted\":{\"date\":" + serializedLocalDateTime + "}}";

        Adopted deserializedAdoptedState = objectMapper.readValue(serializedAdoptedState, Adopted.class);

        assertNotNull(deserializedAdoptedState);
        assertThat(deserializedAdoptedState.getDate(), is(localDateTime));
        assertNull(deserializedAdoptedState.getAdoptionFormId());
    }
}