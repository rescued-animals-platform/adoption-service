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

public class AdoptedTest {

    @Test
    public void shouldBeAnInstanceOfState() {
        Adopted adoptedState = new Adopted(LocalDateTime.now(), randomAlphabetic(10));

        assertThat(adoptedState, is(instanceOf(State.class)));
    }

    @Test
    public void shouldBeSerializable() throws JsonProcessingException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        String adoptionFormId = randomAlphabetic(10);
        LocalDateTime localDateTime = LocalDateTime.now();
        String expectedZonedDateTime = objectMapper.writeValueAsString(DateTimeHelper.getZonedDateTime(localDateTime));
        String expectedSerializedAdoptedState = "{\"adopted\":{\"adoptionFormId\":\"" + adoptionFormId +
                "\",\"date\":" + expectedZonedDateTime + "}}";
        Adopted adoptedState = new Adopted(localDateTime, adoptionFormId);

        String serializedAdoptedState = objectMapper.writeValueAsString(adoptedState);

        assertThat(serializedAdoptedState, is(expectedSerializedAdoptedState));
    }

    @Test
    public void shouldBeDeserializable() throws IOException {
        String adoptionFormId = randomAlphabetic(10);
        String serializedAdoptedState = "{\"adopted\":{\"adoptionFormId\":\"" + adoptionFormId + "\"}}";
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
                )
                .build();

        Adopted deserializedAdoptedState = objectMapper.readValue(serializedAdoptedState, Adopted.class);

        assertNotNull(deserializedAdoptedState);
        assertNotNull(deserializedAdoptedState.getDate());
        assertThat(deserializedAdoptedState.getAdoptionFormId(), is(adoptionFormId));
    }
}