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

package ec.animal.adoption.models.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.exceptions.UnexpectedException;
import ec.animal.adoption.helpers.JsonHelper;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaStateTest {

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(JpaState.class, "objectMapper", JsonHelper.getObjectMapper());
    }

    @Test
    public void shouldCreateAJpaStateFromALookingForHumanState() {
        LookingForHuman lookingForHuman = new LookingForHuman(LocalDateTime.now());
        JpaState jpaState = JpaState.getFor(lookingForHuman, mock(JpaAnimal.class));

        assertReflectionEquals(lookingForHuman, jpaState.toState());
    }

    @Test
    public void shouldCreateAJpaStateFromAnAdoptedState() {
        Adopted adopted = new Adopted(LocalDateTime.now(), randomAlphabetic(10));
        JpaState jpaState = JpaState.getFor(adopted, mock(JpaAnimal.class));

        assertReflectionEquals(adopted, jpaState.toState());
    }

    @Test
    public void shouldCreateAJpaStateFromAnUnavailableState() {
        Unavailable unavailable = new Unavailable(LocalDateTime.now(), randomAlphabetic(20));
        JpaState jpaState = JpaState.getFor(unavailable, mock(JpaAnimal.class));

        assertReflectionEquals(unavailable, jpaState.toState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForAnyOtherState() {
        JpaState.getFor(mock(State.class), mock(JpaAnimal.class));
    }

    @Test(expected = UnexpectedException.class)
    public void shouldThrowUnexpectedExceptionIfJpaStateableCouldNotBeWrittenAsJson() throws JsonProcessingException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.writeValueAsString(any(JpaStateable.class))).thenThrow(JsonProcessingException.class);
        ReflectionTestUtils.setField(JpaState.class, "objectMapper", objectMapper);

        JpaState.getFor(new LookingForHuman(LocalDateTime.now()), mock(JpaAnimal.class));
    }

    @Test(expected = UnexpectedException.class)
    public void shouldThrowUnexpectedExceptionIfJsonCanNotBeRead() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.writeValueAsString(any(JpaStateable.class))).thenReturn(randomAlphabetic(10));
        when(objectMapper.readValue(anyString(), eq(JpaStateable.class))).thenThrow(new IOException());
        ReflectionTestUtils.setField(JpaState.class, "objectMapper", objectMapper);
        JpaState jpaState = JpaState.getFor(new LookingForHuman(LocalDateTime.now()), mock(JpaAnimal.class));

        jpaState.toState();
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaState.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).withPrefabValues(JpaAnimal.class, mock(JpaAnimal.class), mock(JpaAnimal.class)).suppress(
                Warning.REFERENCE_EQUALITY
        ).withPrefabValues(ObjectMapper.class, mock(ObjectMapper.class), mock(ObjectMapper.class)).verify();
    }
}