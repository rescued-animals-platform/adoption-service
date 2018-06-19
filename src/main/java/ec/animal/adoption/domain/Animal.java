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

package ec.animal.adoption.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class Animal {

    @JsonProperty("uuid")
    private UUID uuid;

    @NotEmpty(message = "Animal clinical record is required")
    @JsonProperty("clinicalRecord")
    private final String clinicalRecord;

    @NotEmpty(message = "Animal name is required")
    @JsonProperty("name")
    private final String name;

    @NotNull(message = "Animal registration date is required")
    @JsonProperty("registrationDate")
    private final LocalDateTime registrationDate;

    @NotNull(message = "Animal species is required")
    @JsonProperty("animalSpecies")
    private final Species species;

    @NotNull(message = "Animal estimated age is required")
    @JsonProperty("estimatedAge")
    private final EstimatedAge estimatedAge;

    @NotNull(message = "Animal sex is required")
    @JsonProperty("sex")
    private final Sex sex;

    @JsonProperty("state")
    private final State state;

    @JsonCreator
    public Animal(
            @JsonProperty("clinicalRecord") String clinicalRecord,
            @JsonProperty("name") String name,
            @JsonProperty("registrationDate") LocalDateTime registrationDate,
            @JsonProperty("animalSpecies") Species species,
            @JsonProperty("estimatedAge") EstimatedAge estimatedAge,
            @JsonProperty("sex") Sex sex,
            @JsonProperty("state") State state
    ) {
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.registrationDate = registrationDate;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;

        if(state == null) {
            this.state = new LookingForHuman(registrationDate);
        } else {
            this.state = state;
        }
    }

    public Animal setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getClinicalRecord() {
        return clinicalRecord;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public Species getSpecies() {
        return species;
    }

    public EstimatedAge getEstimatedAge() {
        return estimatedAge;
    }

    public Sex getSex() {
        return sex;
    }

    public State getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        if (uuid != null ? !uuid.equals(animal.uuid) : animal.uuid != null) return false;
        if (clinicalRecord != null ? !clinicalRecord.equals(animal.clinicalRecord) : animal.clinicalRecord != null)
            return false;
        if (name != null ? !name.equals(animal.name) : animal.name != null) return false;
        if (registrationDate != null ? !registrationDate.equals(animal.registrationDate) : animal.registrationDate != null)
            return false;
        if (species != animal.species) return false;
        if (estimatedAge != animal.estimatedAge) return false;
        if (sex != animal.sex) return false;
        return state != null ? state.equals(animal.state) : animal.state == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (clinicalRecord != null ? clinicalRecord.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (estimatedAge != null ? estimatedAge.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
