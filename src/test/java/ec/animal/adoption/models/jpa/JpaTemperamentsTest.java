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

import ec.animal.adoption.builders.TemperamentsBuilder;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

public class JpaTemperamentsTest {

    @Test
    public void shouldCreateJpaTemperamentFromTemperament() {
        Temperaments temperaments = TemperamentsBuilder.random().build();

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperaments);

        assertThat(jpaTemperaments.toTemperaments(), is(temperaments));
    }

    @Test
    public void shouldAcceptNullSociability() {
        Temperaments temperaments = TemperamentsBuilder.random().withSociability(null).build();

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperaments);

        assertNull(jpaTemperaments.toTemperaments().getSociability());
    }

    @Test
    public void shouldAcceptNullDocility() {
        Temperaments temperaments = TemperamentsBuilder.random().withDocility(null).build();

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperaments);

        assertNull(jpaTemperaments.toTemperaments().getDocility());
    }

    @Test
    public void shouldAcceptNullBalance() {
        Temperaments temperaments = TemperamentsBuilder.random().withBalance(null).build();

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperaments);

        assertNull(jpaTemperaments.toTemperaments().getBalance());
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void shouldVerifyEqualsAnsHashCode() {
        EqualsVerifier.forClass(JpaTemperaments.class).usingGetClass().verify();
    }
}