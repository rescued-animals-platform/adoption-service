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
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class JpaAnimalTest {

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimal() {
        Animal animal = AnimalBuilder.random().build();

        JpaAnimal jpaAnimal = new JpaAnimal(animal);
        Animal jpaAnimalToAnimal = jpaAnimal.toAnimal();

        assertNotNull(jpaAnimalToAnimal.getUuid());
        assertNotNull(jpaAnimalToAnimal.getRegistrationDate());
        assertThat(jpaAnimalToAnimal.getClinicalRecord(), is(animal.getClinicalRecord()));
        assertThat(jpaAnimalToAnimal.getName(), is(animal.getName()));
        assertThat(jpaAnimalToAnimal.getSpecies(), is(animal.getSpecies()));
        assertThat(jpaAnimalToAnimal.getEstimatedAge(), is(animal.getEstimatedAge()));
        assertThat(jpaAnimalToAnimal.getSex(), is(animal.getSex()));
        assertThat(jpaAnimalToAnimal.getState(), is(animal.getState()));
    }
}