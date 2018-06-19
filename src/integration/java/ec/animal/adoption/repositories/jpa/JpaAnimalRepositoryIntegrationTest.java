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

import ec.animal.adoption.AbstractIntegrationTest;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Species;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.models.jpa.JpaAnimal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JpaAnimalRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private JpaAnimalRepository jpaAnimalRepository;

    private JpaAnimal entity;

    @Before
    public void setUp() {
        LocalDateTime registrationDate = LocalDateTime.now();
        entity = new JpaAnimal(new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                registrationDate,
                Species.CAT,
                EstimatedAge.YOUNG,
                Sex.MALE,
                new LookingForHuman(registrationDate)
        ));
    }

    @Test
    public void shouldSaveAnAnimalLookingForHuman() {
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);

        assertEquals(jpaAnimal, entity);
    }

    @Test
    public void shouldSaveAnAnimalWithDifferentState() {
        JpaAnimal entity = new JpaAnimal(new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(),
                Species.CAT,
                EstimatedAge.YOUNG_ADULT,
                Sex.FEMALE,
                new Unavailable(randomAlphabetic(10))
        ));

        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);

        assertEquals(jpaAnimal, entity);
    }

    @Test
    public void shouldUpdateAnimalState() {
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        State updatedState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        jpaAnimal.setState(updatedState);

        JpaAnimal adoptedJpaAnimal = jpaAnimalRepository.save(jpaAnimal);

        assertEquals(adoptedJpaAnimal.toAnimal().getState(), updatedState);
    }

    @Test
    public void shouldFindAnimalByAnimalUuid() {
        JpaAnimal jpaAnimal = jpaAnimalRepository.save(entity);
        UUID animalUuid = jpaAnimal.toAnimal().getUuid();

        Optional<JpaAnimal> optionalJpaAnimal = jpaAnimalRepository.findById(animalUuid);

        assertThat(optionalJpaAnimal.isPresent(), is(true));
        assertThat(optionalJpaAnimal.get(), is(jpaAnimal));
    }
}