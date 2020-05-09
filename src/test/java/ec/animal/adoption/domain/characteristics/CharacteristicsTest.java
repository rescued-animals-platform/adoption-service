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

package ec.animal.adoption.domain.characteristics;

import com.google.common.collect.Sets;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.characteristics.temperaments.TemperamentsBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomFriendlyWith;
import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacteristicsTest {

    @Test
    void shouldCreateCharacteristics() {
        UUID characteristicsId = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();
        Size size = getRandomSize();
        PhysicalActivity physicalActivity = getRandomPhysicalActivity();
        Temperaments temperaments = TemperamentsBuilder.random().build();
        Set<FriendlyWith> friendlyWith = Sets.newHashSet(getRandomFriendlyWith());

        Characteristics characteristics = new Characteristics(characteristicsId, registrationDate, size,
                                                              physicalActivity, temperaments, friendlyWith);

        assertAll(
                () -> assertEquals(characteristicsId, characteristics.getIdentifier()),
                () -> assertEquals(registrationDate, characteristics.getRegistrationDate()),
                () -> assertEquals(size, characteristics.getSize()),
                () -> assertEquals(physicalActivity, characteristics.getPhysicalActivity()),
                () -> assertEquals(temperaments, characteristics.getTemperaments()),
                () -> assertEquals(friendlyWith, characteristics.getFriendlyWith())
        );
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Characteristics.class).usingGetClass().verify();
    }
}