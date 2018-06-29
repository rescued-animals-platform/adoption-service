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
import org.junit.Test;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaAdoptedTest {

    @Test
    public void shouldCreateAJpaAdoptedFromAnAdoptedState() {
        Adopted adopted = new Adopted(LocalDateTime.now(), randomAlphabetic(10));
        JpaAdopted jpaAdopted = new JpaAdopted(adopted);

        assertReflectionEquals(adopted, jpaAdopted.toState());
    }
}