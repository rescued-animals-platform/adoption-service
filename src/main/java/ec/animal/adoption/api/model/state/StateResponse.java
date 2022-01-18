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

package ec.animal.adoption.api.model.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.state.State;

public class StateResponse {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("adoptionFormId")
    private final String adoptionFormId;

    @JsonProperty("notes")
    private final String notes;

    @JsonCreator
    private StateResponse(@JsonProperty("name") final String name,
                          @JsonProperty("adoptionFormId") final String adoptionFormId,
                          @JsonProperty("notes") final String notes) {
        this.name = name;
        this.adoptionFormId = adoptionFormId;
        this.notes = notes;
    }

    public static StateResponse from(final State state) {
        return new StateResponse(state.getName().name(),
                                 state.getAdoptionFormId().orElse(null),
                                 state.getNotes().orElse(null));
    }
}
