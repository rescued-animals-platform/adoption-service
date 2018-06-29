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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ec.animal.adoption.helpers.DateTimeHelper;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class UnavailableTest {

    @Test
    public void shouldBeAnInstanceOfState() {
        Unavailable unavailableState = new Unavailable(LocalDateTime.now(), randomAlphabetic(20));

        assertThat(unavailableState, is(instanceOf(State.class)));
    }

    @Test
    public void shouldBeSerializable() throws JsonProcessingException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String notes = randomAlphabetic(20);
        LocalDateTime localDateTime = LocalDateTime.now();
        String expectedZonedDateTime = objectMapper.writeValueAsString(DateTimeHelper.getZonedDateTime(localDateTime));
        String expectedSerializedUnavailableState = "{\"unavailable\":{\"notes\":\"" + notes +
                "\",\"date\":" + expectedZonedDateTime + "}}";
        Unavailable unavailableState = new Unavailable(localDateTime, notes);

        String serializedUnavailableState = objectMapper.writeValueAsString(unavailableState);

        assertThat(serializedUnavailableState, is(expectedSerializedUnavailableState));
    }

    @Test
    public void shouldBeDeserializable() throws IOException {
        String notes = randomAlphabetic(20);
        String serializedUnavailableState = "{\"unavailable\":{\"notes\":\"" + notes + "\"}}";
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
                )
                .build();

        Unavailable deserializedUnavailableState = objectMapper.readValue(
                serializedUnavailableState, Unavailable.class
        );

        assertNotNull(deserializedUnavailableState);
        assertNotNull(deserializedUnavailableState.getDate());
        assertThat(deserializedUnavailableState.getNotes(), is(notes));
    }
}