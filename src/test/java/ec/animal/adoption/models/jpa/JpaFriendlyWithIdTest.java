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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.io.Serializable;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class JpaFriendlyWithIdTest {

    private JpaFriendlyWithId jpaFriendlyWithId;

    @Test
    public void shouldBeAnInstanceOfSerializable() {
        jpaFriendlyWithId = new JpaFriendlyWithId(randomAlphabetic(10), mock(JpaCharacteristics.class));

        assertThat(jpaFriendlyWithId, is(instanceOf(Serializable.class)));
    }

    @Test
    public void shouldCreateJpaFriendlyWithIdWithFriendlyWith() {
        String friendlyWith = randomAlphabetic(10);
        jpaFriendlyWithId = new JpaFriendlyWithId(friendlyWith, mock(JpaCharacteristics.class));

        assertThat(jpaFriendlyWithId.getFriendlyWith(), is(friendlyWith));
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaFriendlyWithId.class).usingGetClass().withPrefabValues(
                JpaCharacteristics.class,
                mock(JpaCharacteristics.class),
                mock(JpaCharacteristics.class)
        ).suppress(Warning.REFERENCE_EQUALITY, Warning.NONFINAL_FIELDS).verify();
    }
}