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
import ec.animal.adoption.domain.state.StateName;
import ec.animal.adoption.validator.ValidCreateStateRequest;

@ValidCreateStateRequest
public class CreateStateRequest {

    @JsonProperty("name")
    private final StateName name;

    @JsonProperty("adoptionFormId")
    private final String adoptionFormId;

    @JsonProperty("notes")
    private final String notes;

    @JsonCreator
    public CreateStateRequest(@JsonProperty("name") final StateName name,
                              @JsonProperty("adoptionFormId") final String adoptionFormId,
                              @JsonProperty("notes") final String notes) {
        this.name = name;
        this.adoptionFormId = adoptionFormId;
        this.notes = notes;
    }

    public State toCreateStateDtoDomain() {
        switch (this.name) {
            case LOOKING_FOR_HUMAN:
                return State.lookingForHuman();
            case ADOPTED:
                return State.adopted(adoptionFormId);
            case UNAVAILABLE:
                return State.unavailable(notes);
            default:
                throw new IllegalStateException();
        }
    }

    public boolean hasName() {
        return name != null;
    }

    public boolean isUnavailableAndDoesNotHaveNotes() {
        return StateName.UNAVAILABLE.equals(name) && notes == null;
    }
}
