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

package ec.animal.adoption.domain.animal.characteristics;

import ec.animal.adoption.domain.animal.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.animal.characteristics.temperaments.TemperamentsFactory;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomFriendlyWith;
import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacteristicsTest {

    private Size size;
    private PhysicalActivity physicalActivity;
    private Temperaments temperaments;
    private Set<FriendlyWith> friendlyWith;

    @BeforeEach
    void setUp() {
        size = getRandomSize();
        physicalActivity = getRandomPhysicalActivity();
        temperaments = TemperamentsFactory.random().build();
        friendlyWith = Set.of(getRandomFriendlyWith());
    }

    @Test
    void shouldCreateCharacteristicsWithNoIdentifierNorRegistrationDate() {
        Characteristics characteristics = new Characteristics(size, physicalActivity, temperaments, friendlyWith);

        assertAll(() -> assertNull(characteristics.getIdentifier()),
                  () -> assertNull(characteristics.getRegistrationDate()),
                  () -> assertEquals(size, characteristics.getSize()),
                  () -> assertEquals(physicalActivity, characteristics.getPhysicalActivity()),
                  () -> assertEquals(temperaments, characteristics.getTemperaments()),
                  () -> assertEquals(friendlyWith, characteristics.getFriendlyWith()));
    }

    @Test
    void shouldCreateCharacteristicsWithIdentifierAndRegistrationDate() {
        UUID characteristicsId = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();

        Characteristics characteristics = new Characteristics(characteristicsId, registrationDate, size,
                                                              physicalActivity, temperaments, friendlyWith);

        assertAll(() -> assertEquals(characteristicsId, characteristics.getIdentifier()),
                  () -> assertEquals(registrationDate, characteristics.getRegistrationDate()),
                  () -> assertEquals(size, characteristics.getSize()),
                  () -> assertEquals(physicalActivity, characteristics.getPhysicalActivity()),
                  () -> assertEquals(temperaments, characteristics.getTemperaments()),
                  () -> assertEquals(friendlyWith, characteristics.getFriendlyWith()));
    }

    @Test
    void shouldUpdateSizePhysicalActivityTemperamentsAndFriendlyWithInCharacteristics() {
        Characteristics characteristics = CharacteristicsFactory.random().build();
        Characteristics newCharacteristics = new Characteristics(size, physicalActivity, temperaments, friendlyWith);

        Characteristics updatedCharacteristics = characteristics.updateWith(newCharacteristics);

        assertAll(
                () -> assertEquals(characteristics.getIdentifier(), updatedCharacteristics.getIdentifier()),
                () -> assertEquals(characteristics.getRegistrationDate(), updatedCharacteristics.getRegistrationDate()),
                () -> assertEquals(size, updatedCharacteristics.getSize()),
                () -> assertEquals(physicalActivity, updatedCharacteristics.getPhysicalActivity()),
                () -> assertEquals(temperaments, updatedCharacteristics.getTemperaments()),
                () -> assertEquals(friendlyWith, updatedCharacteristics.getFriendlyWith())
        );
    }

    @Test
    void shouldReturnSameCharacteristicsWhenUpdatedWithItself() {
        Characteristics characteristics = CharacteristicsFactory.random().build();

        Characteristics updatedCharacteristics = characteristics.updateWith(characteristics);

        assertEquals(characteristics, updatedCharacteristics);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingCharacteristicsThatHaveNoIdentifier() {
        Characteristics characteristics = CharacteristicsFactory.random().withIdentifier(null).build();
        Characteristics characteristicsUpdate = CharacteristicsFactory.random().build();

        assertThrows(IllegalArgumentException.class, () -> characteristics.updateWith(characteristicsUpdate));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingCharacteristicsThatHaveNoRegistrationDate() {
        Characteristics characteristics = CharacteristicsFactory.random().withRegistrationDate(null).build();
        Characteristics characteristicsUpdate = CharacteristicsFactory.random().build();

        assertThrows(IllegalArgumentException.class, () -> characteristics.updateWith(characteristicsUpdate));
    }

    @Test
    void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Characteristics.class).usingGetClass().verify();
    }
}