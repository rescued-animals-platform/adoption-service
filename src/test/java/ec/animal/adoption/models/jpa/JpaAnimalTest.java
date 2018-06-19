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

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Species;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JpaAnimalTest {

    private String clinicalRecord;
    private String name;
    private LocalDateTime registrationDate;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private Animal animal;

    @Before
    public void setUp() {
        clinicalRecord = randomAlphabetic(10);
        name = randomAlphabetic(10);
        registrationDate = LocalDateTime.now();
        species = TestUtils.getRandomSpecies();
        estimatedAge = TestUtils.getRandomEstimatedAge();
        sex = TestUtils.getRandomSex();
        State state = TestUtils.getRandomState();
        animal = new Animal(clinicalRecord, name, registrationDate, species, estimatedAge, sex, state);
    }

    @Test
    public void shouldCreateAJpaAnimalFromAnAnimal() {
        JpaAnimal jpaAnimal = new JpaAnimal(animal);

        Animal animal = jpaAnimal.toAnimal();
        assertThat(animal.getClinicalRecord(), is(this.animal.getClinicalRecord()));
        assertThat(animal.getName(), is(this.animal.getName()));
        assertThat(animal.getRegistrationDate(), is(this.animal.getRegistrationDate()));
        assertThat(animal.getSpecies(), is(this.animal.getSpecies()));
        assertThat(animal.getEstimatedAge(), is(this.animal.getEstimatedAge()));
        assertThat(animal.getSex(), is(this.animal.getSex()));
        assertThat(animal.getState(), is(this.animal.getState()));
    }

    @Test
    public void shouldUpdateState() {
        animal = new Animal(
                clinicalRecord, name, registrationDate, species, estimatedAge, sex, new LookingForHuman(registrationDate)
        );
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