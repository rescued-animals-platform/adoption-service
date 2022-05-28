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

package ec.animal.adoption.domain.model.animal.dto;

import ec.animal.adoption.domain.model.animal.EstimatedAge;
import ec.animal.adoption.domain.model.animal.Sex;
import ec.animal.adoption.domain.model.animal.Species;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.state.State;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.UUID;

public record AnimalDto(String clinicalRecord,
                        String name,
                        Species species,
                        EstimatedAge estimatedAge,
                        Sex sex,
                        State state,
                        Organization organization) {

    public AnimalDto(@NonNull String clinicalRecord,
                     @Nullable String name,
                     @NonNull Species species,
                     @NonNull EstimatedAge estimatedAge,
                     @NonNull Sex sex,
                     @Nullable State state,
                     @NonNull Organization organization) {
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
        this.state = state == null ? State.lookingForHuman() : state;
        this.organization = organization;
    }

    public Optional<String> getAdoptionFormId() {
        return Optional.of(this.state).flatMap(State::getAdoptionFormId);
    }

    public Optional<String> getNotes() {
        return Optional.of(this.state).flatMap(State::getNotes);
    }

    @NonNull
    public String getStateNameAsString() {
        return this.state.getName().name();
    }

    @NonNull
    public UUID getOrganizationId() {
        return organization.getOrganizationId();
    }
}
