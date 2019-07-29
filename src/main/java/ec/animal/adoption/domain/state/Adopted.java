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

package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Adopted extends State {

    private transient static final long serialVersionUID = -212446656134428610L;

    @JsonProperty("adoptionFormId")
    private final String adoptionFormId;

    @JsonCreator
    public Adopted(
            @JsonProperty("date") final LocalDateTime date,
            @JsonProperty("adoptionFormId") final String adoptionFormId
    ) {
        super(date);
        this.adoptionFormId = adoptionFormId;
    }

    public String getAdoptionFormId() {
        return adoptionFormId;
    }
}
