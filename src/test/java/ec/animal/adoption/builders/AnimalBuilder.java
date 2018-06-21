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

package ec.animal.adoption.builders;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Species;
import ec.animal.adoption.domain.state.State;

import java.time.LocalDateTime;

import static ec.animal.adoption.TestUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class AnimalBuilder {

    private String clinicalRecord;
    private String name;
    private LocalDateTime registrationDate;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private State state;

    public static AnimalBuilder random() {
        AnimalBuilder animalBuilder = new AnimalBuilder();
        animalBuilder.clinicalRecord = randomAlphabetic(10);
        animalBuilder.name = randomAlphabetic(10);
        animalBuilder.registrationDate = LocalDateTime.now();
        animalBuilder.species = getRandomSpecies();
        animalBuilder.estimatedAge = getRandomEstimatedAge();
        animalBuilder.sex = getRandomSex();
        animalBuilder.state = getRandomState();
        return animalBuilder;
    }

    public AnimalBuilder withClinicalRecord(String clinicalRecord) {
        this.clinicalRecord = clinicalRecord;
        return this;
    }

    public AnimalBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AnimalBuilder withRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public AnimalBuilder withSpecies(Species species) {
        this.species = species;
        return this;
    }

    public AnimalBuilder withEstimatedAge(EstimatedAge estimatedAge) {
        this.estimatedAge = estimatedAge;
        return this;
    }

    public AnimalBuilder withSex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public AnimalBuilder withState(State state) {
        this.state = state;
        return this;
    }

    public Animal build() {
        return new Animal(
                this.clinicalRecord,
                this.name,
                this.registrationDate,
                this.species,
                this.estimatedAge,
                this.sex,
                this.state
        );
    }
}
