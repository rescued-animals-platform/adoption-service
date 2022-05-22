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

package ec.animal.adoption.repository.jpa.model;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.CharacteristicsFactory;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.characteristics.temperaments.TemperamentsFactory;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class JpaCharacteristicsTest {

    @Test
    void shouldGenerateAnIdWhenCreatingJpaCharacteristicsForCharacteristicsWithNoId() {
        Characteristics characteristics = CharacteristicsFactory.random().withIdentifier(null).build();
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics, mock(JpaAnimal.class));

        Characteristics jpaCharacteristicsToCharacteristics = jpaCharacteristics.toCharacteristics();

        assertNotNull(jpaCharacteristicsToCharacteristics.getIdentifier());
    }

    @Test
    void shouldGenerateARegistrationDateWhenCreatingJpaCharacteristicsForCharacteristicsWithNoRegistrationDate() {
        Characteristics characteristics = CharacteristicsFactory.random().withRegistrationDate(null).build();
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics, mock(JpaAnimal.class));

        Characteristics jpaCharacteristicsToCharacteristics = jpaCharacteristics.toCharacteristics();

        assertNotNull(jpaCharacteristicsToCharacteristics.getRegistrationDate());
    }

    @Test
    void shouldCreateCharacteristicsWithId() {
        UUID characteristicsId = UUID.randomUUID();
        Characteristics characteristics = CharacteristicsFactory.random().withIdentifier(characteristicsId).build();
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics, mock(JpaAnimal.class));

        Characteristics jpaCharacteristicsToCharacteristics = jpaCharacteristics.toCharacteristics();

        assertThat(jpaCharacteristicsToCharacteristics.getIdentifier(), is(characteristicsId));
    }

    @Test
    void shouldCreateCharacteristicsWithRegistrationDate() {
        LocalDateTime registrationDate = LocalDateTime.now();
        Characteristics characteristics = CharacteristicsFactory.random().withRegistrationDate(registrationDate)
                                                                .build();
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics, mock(JpaAnimal.class));

        Characteristics jpaCharacteristicsToCharacteristics = jpaCharacteristics.toCharacteristics();

        assertThat(jpaCharacteristicsToCharacteristics.getRegistrationDate(), is(registrationDate));
    }

    @Test
    void shouldCreateJpaCharacteristicsFromCharacteristics() {
        Characteristics characteristics = CharacteristicsFactory.random().build();
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics, mock(JpaAnimal.class));

        Characteristics jpaCharacteristicsToCharacteristics = jpaCharacteristics.toCharacteristics();

        assertThat(jpaCharacteristicsToCharacteristics.getSize(), is(characteristics.getSize()));
        assertThat(jpaCharacteristicsToCharacteristics.getTemperaments(), is(characteristics.getTemperaments()));
        assertThat(jpaCharacteristicsToCharacteristics.getFriendlyWith(), is(characteristics.getFriendlyWith()));
        assertThat(jpaCharacteristicsToCharacteristics.getPhysicalActivity(), is(characteristics.getPhysicalActivity()));
    }

    @Test
    void shouldAcceptNullSociability() {
        Temperaments temperaments = TemperamentsFactory.random().withSociability(null).build();
        Characteristics characteristics = CharacteristicsFactory.random().withTemperaments(temperaments).build();

        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics, mock(JpaAnimal.class));
        Characteristics characteristicsFromJpaCharacteristics = jpaCharacteristics.toCharacteristics();

        assertTrue(characteristicsFromJpaCharacteristics.getTemperaments().getSociability().isEmpty());
    }

    @Test
    void shouldAcceptNullDocility() {
        Temperaments temperaments = TemperamentsFactory.random().withDocility(null).build();
        Characteristics characteristics = CharacteristicsFactory.random().withTemperaments(temperaments).build();

        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics, mock(JpaAnimal.class));
        Characteristics characteristicsFromJpaCharacteristics = jpaCharacteristics.toCharacteristics();

        assertTrue(characteristicsFromJpaCharacteristics.getTemperaments().getDocility().isEmpty());
    }

    @Test
    void shouldAcceptNullBalance() {
        Temperaments temperaments = TemperamentsFactory.random().withBalance(null).build();
        Characteristics characteristics = CharacteristicsFactory.random().withTemperaments(temperaments).build();

        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics, mock(JpaAnimal.class));
        Characteristics characteristicsFromJpaCharacteristics = jpaCharacteristics.toCharacteristics();

        assertTrue(characteristicsFromJpaCharacteristics.getTemperaments().getBalance().isEmpty());
    }

    @Test
    void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaCharacteristics.class).usingGetClass()
                      .withPrefabValues(
                              JpaFriendlyWith.class, mock(JpaFriendlyWith.class), mock(JpaFriendlyWith.class)
                      ).withPrefabValues(JpaAnimal.class, mock(JpaAnimal.class), mock(JpaAnimal.class))
                      .suppress(Warning.NONFINAL_FIELDS, Warning.REFERENCE_EQUALITY, Warning.SURROGATE_KEY).verify();
    }
}