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

package ec.animal.adoption.adapter.rest.model.animal;

import ec.animal.adoption.adapter.rest.model.state.StateRequest;
import ec.animal.adoption.domain.model.animal.EstimatedAge;
import ec.animal.adoption.domain.model.animal.Sex;
import ec.animal.adoption.domain.model.animal.Species;
import ec.animal.adoption.domain.model.state.State;

import static ec.animal.adoption.TestUtils.getRandomEstimatedAge;
import static ec.animal.adoption.TestUtils.getRandomSex;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomStateName;
import static ec.animal.adoption.domain.model.state.StateName.ADOPTED;
import static ec.animal.adoption.domain.model.state.StateName.LOOKING_FOR_HUMAN;
import static ec.animal.adoption.domain.model.state.StateName.UNAVAILABLE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class CreateAnimalRequestBuilder {

    private String clinicalRecord;
    private String name;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private StateRequest stateRequest;

    public static CreateAnimalRequestBuilder random() {
        CreateAnimalRequestBuilder createAnimalRequestBuilder = new CreateAnimalRequestBuilder();
        createAnimalRequestBuilder.clinicalRecord = randomAlphabetic(10);
        createAnimalRequestBuilder.name = randomAlphabetic(10);
        createAnimalRequestBuilder.species = getRandomSpecies();
        createAnimalRequestBuilder.estimatedAge = getRandomEstimatedAge();
        createAnimalRequestBuilder.sex = getRandomSex();
        createAnimalRequestBuilder.stateRequest = new StateRequest(getRandomStateName(),
                                                                   randomAlphabetic(10),
                                                                   randomAlphabetic(10));
        return createAnimalRequestBuilder;
    }

    public CreateAnimalRequestBuilder withClinicalRecord(final String clinicalRecord) {
        this.clinicalRecord = clinicalRecord;
        return this;
    }

    public CreateAnimalRequestBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CreateAnimalRequestBuilder withSpecies(final Species species) {
        this.species = species;
        return this;
    }

    public CreateAnimalRequestBuilder withEstimatedAge(final EstimatedAge estimatedAge) {
        this.estimatedAge = estimatedAge;
        return this;
    }

    public CreateAnimalRequestBuilder withSex(final Sex sex) {
        this.sex = sex;
        return this;
    }

    public CreateAnimalRequestBuilder withState(final State state) {
        if (state == null) {
            this.stateRequest = null;
            return this;
        }

        switch (state.getName().name()) {
            case "LOOKING_FOR_HUMAN":
                this.stateRequest = new StateRequest(LOOKING_FOR_HUMAN, null, null);
                break;
            case "ADOPTED":
                this.stateRequest = new StateRequest(ADOPTED, state.getAdoptionFormId().orElse(null), null);
                break;
            case "UNAVAILABLE":
                this.stateRequest = new StateRequest(UNAVAILABLE, null, state.getNotes().orElse(null));
                break;
            default:
                this.stateRequest = null;
                break;
        }

        return this;
    }

    public CreateAnimalRequestBuilder withCreateStateRequest(final StateRequest stateRequest) {
        this.stateRequest = stateRequest;
        return this;
    }

    public AnimalCreateUpdateRequest build() {
        return new AnimalCreateUpdateRequest(this.clinicalRecord,
                                             this.name,
                                             this.species,
                                             this.estimatedAge,
                                             this.sex,
                                             this.stateRequest);
    }
}
