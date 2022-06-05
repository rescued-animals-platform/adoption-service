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

import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.adapter.rest.model.state.StateRequest;
import ec.animal.adoption.domain.model.animal.EstimatedAge;
import ec.animal.adoption.domain.model.animal.Sex;
import ec.animal.adoption.domain.model.animal.Species;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record AnimalCreateUpdateRequest(
        @NotEmpty(message = "Animal clinical record is required") String clinicalRecord,
        @Size(min = 1, message = "Animal name must be a non-empty string") @Pattern(regexp = "^(?!\\s*$).+$", message = "Animal name must be a non-empty string") String name,
        @NotNull(message = "Animal species is required") Species species,
        @NotNull(message = "Animal estimated age is required") EstimatedAge estimatedAge,
        @NotNull(message = "Animal sex is required") Sex sex,
        @Valid @JsonProperty("state") StateRequest stateRequest) {
}
