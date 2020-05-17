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

package ec.animal.adoption.domain.animal.dto;

import ec.animal.adoption.domain.animal.EstimatedAge;
import ec.animal.adoption.domain.animal.Sex;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import ec.animal.adoption.domain.state.State;

import static ec.animal.adoption.TestUtils.getRandomEstimatedAge;
import static ec.animal.adoption.TestUtils.getRandomSex;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomState;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class AnimalDtoFactory {

    private String clinicalRecord;
    private String name;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private State state;
    private Organization organization;

    public static AnimalDtoFactory random() {
        AnimalDtoFactory animalDtoFactory = new AnimalDtoFactory();
        animalDtoFactory.clinicalRecord = randomAlphabetic(10);
        animalDtoFactory.name = randomAlphabetic(10);
        animalDtoFactory.species = getRandomSpecies();
        animalDtoFactory.estimatedAge = getRandomEstimatedAge();
        animalDtoFactory.sex = getRandomSex();
        animalDtoFactory.state = getRandomState();
        animalDtoFactory.organization = OrganizationFactory.random().build();
        return animalDtoFactory;
    }

    public static AnimalDtoFactory randomWithDefaultOrganization() {
        AnimalDtoFactory animalDtoFactory = random();
        animalDtoFactory.organization = OrganizationFactory.randomDefaultOrganization().build();
        return animalDtoFactory;
    }

    public static AnimalDtoFactory randomWithAnotherOrganization() {
        AnimalDtoFactory animalDtoFactory = random();
        animalDtoFactory.organization = OrganizationFactory.randomAnotherOrganization().build();
        return animalDtoFactory;
    }

    public AnimalDtoFactory withClinicalRecord(final String clinicalRecord) {
        this.clinicalRecord = clinicalRecord;
        return this;
    }

    public AnimalDtoFactory withName(final String name) {
        this.name = name;
        return this;
    }

    public AnimalDtoFactory withSpecies(final Species species) {
        this.species = species;
        return this;
    }

    public AnimalDtoFactory withEstimatedAge(final EstimatedAge estimatedAge) {
        this.estimatedAge = estimatedAge;
        return this;
    }

    public AnimalDtoFactory withSex(final Sex sex) {
        this.sex = sex;
        return this;
    }

    public AnimalDtoFactory withState(final State state) {
        this.state = state;
        return this;
    }

    public AnimalDtoFactory withOrganization(final Organization organization) {
        this.organization = organization;
        return this;
    }

    public AnimalDto build() {
        return new AnimalDto(this.clinicalRecord,
                             this.name,
                             this.species,
                             this.estimatedAge,
                             this.sex,
                             this.state,
                             this.organization);
    }
}
