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

import java.io.IOException;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LookingForHumanTest {

    private final ObjectMapper objectMapper = TestUtils.getObjectMapper();

    @Test
    public void shouldBeAnInstanceOfState() {
        LookingForHuman lookingForHumanState = new LookingForHuman(now());

        assertThat(lookingForHumanState, is(instanceOf(State.class)));
    }

    @Test
    void shouldReturnLookingForHumanName() {
        State lookingForHumanState = new LookingForHuman(now());

        assertEquals(lookingForHumanState.getName(), "lookingForHuman");
    }

    @Test
    void shouldSetRegistrationDateIfNull() {
        State lookingForHumanState = new LookingForHuman(null);

        assertNotNull(lookingForHumanState.getRegistrationDate());
    }

    @Test
    public void shouldBeSerializable() throws JsonProcessingException {
        LocalDateTime localDateTime = now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        String expectedSerializedLookingForHumanState = "{\"date\":" + serializedLocalDateTime +
                ",\"name\":\"Looking for human\"}";
        State lookingForHumanState = new LookingForHuman(localDateTime);

        String serializedLookingForHumanState = objectMapper.writeValueAsString(lookingForHumanState);

        assertThat(serializedLookingForHumanState, is(expectedSerializedLookingForHumanState));
    }

    @Test
    public void shouldBeDeserializableWithoutDate() throws IOException {
        String serializedLookingForHumanState = "{\"name\":\"lookingForHuman\"}";

        State deserializedLookingForHumanState = objectMapper.readValue(
                serializedLookingForHumanState, LookingForHuman.class
        );

        assertNotNull(deserializedLookingForHumanState);
        assertEquals("lookingForHuman", deserializedLookingForHumanState.getName());
        assertNotNull(deserializedLookingForHumanState.getRegistrationDate());
    }

    @Test
    public void shouldBeDeserializableWithDate() throws IOException {
        LocalDateTime localDateTime = now();
        String serializedLocalDateTime = objectMapper.writeValueAsString(localDateTime);
        String serializedLookingForHumanState = "{\"name\":\"lookingForHuman\",\"date\":" + serializedLocalDateTime + "}";

        LookingForHuman deserializedLookingForHumanState = objectMapper.readValue(
                serializedLookingForHumanState, LookingForHuman.class
        );

        assertNotNull(deserializedLookingForHumanState);
        assertThat(deserializedLookingForHumanState.getRegistrationDate(), is(localDateTime));
    }
}