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

import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.State;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JpaAnimalTest {

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimal() {
        Animal animal = AnimalBuilder.random().build();
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();
        assertThat(jpaAnimalToAnimal.getClinicalRecord(), is(animal.getClinicalRecord()));
        assertThat(jpaAnimalToAnimal.getName(), is(animal.getName()));
        assertThat(jpaAnimalToAnimal.getRegistrationDate(), is(animal.getRegistrationDate()));
        assertThat(jpaAnimalToAnimal.getSpecies(), is(animal.getSpecies()));
        assertThat(jpaAnimalToAnimal.getEstimatedAge(), is(animal.getEstimatedAge()));
        assertThat(jpaAnimalToAnimal.getSex(), is(animal.getSex()));
        assertThat(jpaAnimalToAnimal.getState(), is(animal.getState()));
    }

    @Test
    public void shouldUpdateState() {
        Animal animal = AnimalBuilder.random().build();
        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        State newState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        jpaAnimal.setState(newState);

        assertThat(jpaAnimal.toAnimal().getState(), is(newState));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaAnimal.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).verify();
    }
}