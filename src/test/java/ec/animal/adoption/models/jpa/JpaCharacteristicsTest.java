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

import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.domain.characteristics.Characteristics;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class JpaCharacteristicsTest {

    @Test
    public void shouldCreateJpaCharacteristicsFromCharacteristics() {
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        characteristics.setAnimalUuid(UUID.randomUUID());
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics);

        assertThat(jpaCharacteristics.toCharacteristics(), is(characteristics));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaCharacteristics.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).withPrefabValues(
                JpaFriendlyWith.class, mock(JpaFriendlyWith.class), mock(JpaFriendlyWith.class)
        ).verify();
    }
}