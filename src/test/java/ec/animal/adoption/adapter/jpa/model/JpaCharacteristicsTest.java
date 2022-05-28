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

package ec.animal.adoption.adapter.jpa.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class JpaCharacteristicsTest {

    @Test
    void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaCharacteristics.class)
                      .withPrefabValues(
                              JpaFriendlyWith.class, mock(JpaFriendlyWith.class), mock(JpaFriendlyWith.class)
                      ).withPrefabValues(JpaAnimal.class, mock(JpaAnimal.class), mock(JpaAnimal.class))
                      .suppress(Warning.NONFINAL_FIELDS, Warning.REFERENCE_EQUALITY, Warning.SURROGATE_KEY).verify();
    }
}