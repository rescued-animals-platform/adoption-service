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

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class JpaFriendlyWithTest {

    @Test
    public void shouldCreateJpaFriendlyWithFromFriendlyWithAndJpaCharacteristics() {
        FriendlyWith friendlyWith = TestUtils.getRandomFriendlyWith();

        JpaFriendlyWith jpaFriendlyWith = new JpaFriendlyWith(friendlyWith, mock(JpaCharacteristics.class));

        assertThat(jpaFriendlyWith.toFriendlyWith(), is(friendlyWith));
    }

    @Test
    public void shouldVerifyEqualsAnsHashCode() {
        EqualsVerifier.forClass(JpaFriendlyWith.class).usingGetClass().withPrefabValues(
                JpaCharacteristics.class,
                mock(JpaCharacteristics.class),
                mock(JpaCharacteristics.class)
        ).verify();
    }
}