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
import ec.animal.adoption.domain.organization.OrganizationBuilder;
import ec.animal.adoption.domain.state.State;

import static ec.animal.adoption.TestUtils.getRandomEstimatedAge;
import static ec.animal.adoption.TestUtils.getRandomSex;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomState;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class CreateAnimalDtoBuilder {

    private String clinicalRecord;
    private String name;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private State state;
    private Organization organization;

    public static CreateAnimalDtoBuilder random() {
        CreateAnimalDtoBuilder createAnimalDtoBuilder = new CreateAnimalDtoBuilder();
        createAnimalDtoBuilder.clinicalRecord = randomAlphabetic(10);
        createAnimalDtoBuilder.name = randomAlphabetic(10);
        createAnimalDtoBuilder.species = getRandomSpecies();
        createAnimalDtoBuilder.estimatedAge = getRandomEstimatedAge();
        createAnimalDtoBuilder.sex = getRandomSex();
        createAnimalDtoBuilder.state = getRandomState();
        createAnimalDtoBuilder.organization = OrganizationBuilder.random().build();
        return createAnimalDtoBuilder;
    }

    public static CreateAnimalDtoBuilder randomWithDefaultOrganization() {
        CreateAnimalDtoBuilder createAnimalDtoBuilder = random();
        createAnimalDtoBuilder.organization = OrganizationBuilder.randomDefaultOrganization().build();
        return createAnimalDtoBuilder;
    }

    public static CreateAnimalDtoBuilder randomWithAnotherOrganization() {
        CreateAnimalDtoBuilder createAnimalDtoBuilder = random();
        createAnimalDtoBuilder.organization = OrganizationBuilder.randomAnotherOrganization().build();
        return createAnimalDtoBuilder;
    }

    public CreateAnimalDtoBuilder withClinicalRecord(final String clinicalRecord) {
        this.clinicalRecord = clinicalRecord;
        return this;
    }

    public CreateAnimalDtoBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CreateAnimalDtoBuilder withSpecies(final Species species) {
        this.species = species;
        return this;
    }

    public CreateAnimalDtoBuilder withEstimatedAge(final EstimatedAge estimatedAge) {
        this.estimatedAge = estimatedAge;
        return this;
    }

    public CreateAnimalDtoBuilder withSex(final Sex sex) {
        this.sex = sex;
        return this;
    }

    public CreateAnimalDtoBuilder withState(final State state) {
        this.state = state;
        return this;
    }

    public CreateAnimalDtoBuilder withOrganization(final Organization organization) {
        this.organization = organization;
        return this;
    }

    public CreateAnimalDto build() {
        return new CreateAnimalDto(this.clinicalRecord,
                                   this.name,
                                   this.species,
                                   this.estimatedAge,
                                   this.sex,
                                   this.state,
                                   this.organization);
    }
}
