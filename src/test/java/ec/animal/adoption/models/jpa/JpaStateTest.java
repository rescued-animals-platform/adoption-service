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

import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.Unavailable;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaStateTest {

    private LocalDateTime localDateTime;

    @Before
    public void setUp() {
        localDateTime = LocalDateTime.now();
    }

    @Test
    public void shouldCreateAJpaStateFromALookingForHumanState() {
        LookingForHuman lookingForHuman = new LookingForHuman(localDateTime);
        JpaState jpaState = new JpaState(lookingForHuman, mock(JpaAnimal.class));

        assertReflectionEquals(lookingForHuman, jpaState.toState());
    }

    @Test
    public void shouldCreateAJpaStateFromAnAdoptedState() {
        Adopted adopted = new Adopted(localDateTime, randomAlphabetic(10));
        JpaState jpaState = new JpaState(adopted, mock(JpaAnimal.class));

        assertReflectionEquals(adopted, jpaState.toState());
    }

    @Test
    public void shouldCreateAJpaStateFromAnUnavailableState() {
        Unavailable unavailable = new Unavailable(localDateTime, randomAlphabetic(20));
        JpaState jpaState = new JpaState(unavailable, mock(JpaAnimal.class));

        assertReflectionEquals(unavailable, jpaState.toState());
    }
}