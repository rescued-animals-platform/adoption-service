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

package ec.animal.adoption.adapter.rest.model.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ec.animal.adoption.domain.model.state.StateName;

@ValidStateRequest
public record StateRequest(StateName name,
                           String adoptionFormId,
                           String notes) {

    @JsonIgnore
    public boolean hasName() {
        return name != null;
    }

    @JsonIgnore
    public boolean isUnavailableAndDoesNotHaveNotes() {
        return StateName.UNAVAILABLE.equals(name) && notes == null;
    }
}
