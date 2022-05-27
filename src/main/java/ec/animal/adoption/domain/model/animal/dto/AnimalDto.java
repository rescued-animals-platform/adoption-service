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

public class AnimalDto {

    private final String clinicalRecord;
    private final String name;
    private final Species species;
    private final EstimatedAge estimatedAge;
    private final Sex sex;
    private final State state;
    private final Organization organization;

    public AnimalDto(@NonNull final String clinicalRecord,
                     @Nullable final String name,
                     @NonNull final Species species,
                     @NonNull final EstimatedAge estimatedAge,
                     @NonNull final Sex sex,
                     @Nullable final State state,
                     @NonNull final Organization organization) {
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
        this.state = state == null ? State.lookingForHuman() : state;
        this.organization = organization;
    }

    @NonNull
    public String getClinicalRecord() {
        return clinicalRecord;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @NonNull
    public Species getSpecies() {
        return species;
    }

    @NonNull
    public EstimatedAge getEstimatedAge() {
        return estimatedAge;
    }

    @NonNull
    public Sex getSex() {
        return sex;
    }

    @NonNull
    public State getState() {
        return state;
    }

    @NonNull
    public String getStateNameAsString() {
        return state.getName().name();
    }

    public Optional<String> getAdoptionFormId() {
        return Optional.ofNullable(state).flatMap(State::getAdoptionFormId);
    }

    public Optional<String> getNotes() {
        return Optional.ofNullable(state).flatMap(State::getNotes);
    }

    @NonNull
    public Organization getOrganization() {
        return organization;
    }

    @NonNull
    public UUID getOrganizationId() {
        return organization.getOrganizationId();
    }
}
