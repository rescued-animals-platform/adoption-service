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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.models.jpa.JpaAnimal;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class JpaAnimalRepositoryIntegrationTest extends JpaRepositoryIntegrationTest {

    @Test
    public void shouldSaveAnAnimal() {
        JpaAnimal entity = new JpaAnimal(AnimalBuilder.random().build());
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);

        assertReflectionEquals(jpaAnimal, entity);
    }

    @Test
    public void shouldFindAnimalByAnimalUuid() {
        JpaAnimal entity = new JpaAnimal(AnimalBuilder.random().build());
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();

        Optional<JpaAnimal> optionalJpaAnimal = jpaAnimalRepository.findById(animalUuid);

        assertThat(optionalJpaAnimal.isPresent(), is(true));
        assertReflectionEquals(optionalJpaAnimal.get(), jpaAnimal);
    }

    @Test
    public void shouldFindAllAnimals() {
        ArrayList<JpaAnimal> expectedJpaAnimals = newArrayList(
                new JpaAnimal(AnimalBuilder.random().build()),
                new JpaAnimal(AnimalBuilder.random().build()),
                new JpaAnimal(AnimalBuilder.random().build())
        );
        expectedJpaAnimals.forEach(jpaAnimal -> jpaAnimalRepository.save(jpaAnimal));

        assertReflectionEquals(expectedJpaAnimals, jpaAnimalRepository.findAll());
    }
}