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

package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.builders.CharacteristicsBuilder;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaCharacteristicsRepositoryIntegrationTest extends AbstractJpaRepositoryIntegrationTest {

    @Autowired
    private JpaCharacteristicsRepository jpaCharacteristicsRepository;

    private JpaAnimal jpaAnimal;
    private JpaCharacteristics entity;

    @Before
    public void setUp() {
        jpaAnimal = createAndSaveJpaAnimal();
    }

    @Test
    public void shouldSaveCharacteristics() {
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        characteristics.setAnimalUuid(jpaAnimal.toAnimal().getUuid());
        entity = new JpaCharacteristics(characteristics);

        JpaCharacteristics jpaCharacteristics = jpaCharacteristicsRepository.save(entity);

        assertEquals(jpaCharacteristics, entity);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldThrowDataIntegrityViolationExceptionWhenCreatingCharacteristicsForSameAnimal() {
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        characteristics.setAnimalUuid(jpaAnimal.toAnimal().getUuid());
        entity = new JpaCharacteristics(characteristics);
        jpaCharacteristicsRepository.save(entity);

        jpaCharacteristicsRepository.save(new JpaCharacteristics(characteristics));
    }

    @Test
    public void shouldGetCharacteristicsByAnimalUuid() {
        Characteristics characteristics = CharacteristicsBuilder.random().build();
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();
        characteristics.setAnimalUuid(animalUuid);
        entity = new JpaCharacteristics(characteristics);
        JpaCharacteristics jpaCharacteristicsSaved = jpaCharacteristicsRepository.save(entity);

        Optional<JpaCharacteristics> jpaCharacteristicsFound = jpaCharacteristicsRepository.findByAnimalUuid(animalUuid);

        assertThat(jpaCharacteristicsFound.isPresent(), is(true));
        assertReflectionEquals(jpaCharacteristicsSaved, jpaCharacteristicsFound.get());
    }
}