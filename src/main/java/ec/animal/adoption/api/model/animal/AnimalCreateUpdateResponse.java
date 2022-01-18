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

package ec.animal.adoption.api.model.animal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.api.model.state.StateResponse;
import ec.animal.adoption.domain.animal.Animal;

import java.time.LocalDateTime;
import java.util.UUID;

public class AnimalCreateUpdateResponse {

    @JsonProperty("id")
    private final UUID animalId;

    @JsonProperty("registrationDate")
    private final LocalDateTime registrationDate;

    @JsonProperty("clinicalRecord")
    private final String clinicalRecord;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("species")
    private final String species;

    @JsonProperty("estimatedAge")
    private final String estimatedAge;

    @JsonProperty("sex")
    private final String sex;

    @JsonProperty("state")
    private final StateResponse state;

    @JsonCreator
    private AnimalCreateUpdateResponse(@JsonProperty("id") final UUID animalId,
                                       @JsonProperty("registrationDate") final LocalDateTime registrationDate,
                                       @JsonProperty("clinicalRecord") final String clinicalRecord,
                                       @JsonProperty("name") final String name,
                                       @JsonProperty("species") final String species,
                                       @JsonProperty("estimatedAge") final String estimatedAge,
                                       @JsonProperty("sex") final String sex,
                                       @JsonProperty("state") final StateResponse state) {
        this.animalId = animalId;
        this.registrationDate = registrationDate;
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
        this.state = state;
    }

    public static AnimalCreateUpdateResponse from(final Animal animal) {
        return new AnimalCreateUpdateResponse(animal.getIdentifier(),
                                              animal.getRegistrationDate(),
                                              animal.getClinicalRecord(),
                                              animal.getName(),
                                              animal.getSpecies().name(),
                                              animal.getEstimatedAge().name(),
                                              animal.getSex().name(),
                                              StateResponse.from(animal.getState()));
    }

    public UUID getAnimalId() {
        return animalId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public String getClinicalRecord() {
        return clinicalRecord;
    }
}
