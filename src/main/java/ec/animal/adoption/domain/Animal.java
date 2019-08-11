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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("PMD.DataClass")
public class Animal extends Entity {

    @NotEmpty(message = "Animal clinical record is required")
    @JsonProperty("clinicalRecord")
    private final String clinicalRecord;

    @NotEmpty(message = "Animal name is required")
    @JsonProperty("name")
    private final String name;

    @NotNull(message = "Animal species is required")
    @JsonProperty("species")
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
    private Animal(
            @JsonProperty("clinicalRecord") final String clinicalRecord,
            @JsonProperty("name") final String name,
            @JsonProperty("species") final Species species,
            @JsonProperty("estimatedAge") final EstimatedAge estimatedAge,
            @JsonProperty("sex") final Sex sex,
            @JsonProperty("state") final State state
    ) {
        super(null, null);
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;

        if (state == null) {
            this.state = new LookingForHuman(LocalDateTime.now());
        } else {
            this.state = state;
        }
    }

    public Animal(
            final UUID uuid,
            final LocalDateTime registrationDate,
            final String clinicalRecord,
            final String name,
            final Species species,
            final EstimatedAge estimatedAge,
            final Sex sex,
            final State state
    ) {
        super(uuid, registrationDate);
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
        this.state = state;
    }

    public String getClinicalRecord() {
        return clinicalRecord;
    }

    public String getName() {
        return name;
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

    @JsonIgnore
    public String getStateName() {
        return state.getStateName();
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Animal animal = (Animal) o;

        if (clinicalRecord != null ? !clinicalRecord.equals(animal.clinicalRecord) : animal.clinicalRecord != null)
            return false;
        if (name != null ? !name.equals(animal.name) : animal.name != null) return false;
        if (species != animal.species) return false;
        if (estimatedAge != animal.estimatedAge) return false;
        if (sex != animal.sex) return false;
        return state != null ? state.equals(animal.state) : animal.state == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (clinicalRecord != null ? clinicalRecord.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (estimatedAge != null ? estimatedAge.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
