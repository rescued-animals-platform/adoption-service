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

package ec.animal.adoption.adapter.rest.model.characteristics;

import ec.animal.adoption.adapter.rest.model.characteristics.temperaments.TemperamentsRequest;
import ec.animal.adoption.domain.model.characteristics.FriendlyWith;
import ec.animal.adoption.domain.model.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.model.characteristics.Size;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public record CharacteristicsRequest(@NotNull(message = "Size is required") Size size,
                                     @NotNull(message = "Physical activity is required") PhysicalActivity physicalActivity,
                                     @Valid @NotNull(message = "At least one temperament is required") TemperamentsRequest temperaments,
                                     Set<FriendlyWith> friendlyWith) {

    public CharacteristicsRequest(Size size,
                                  PhysicalActivity physicalActivity,
                                  TemperamentsRequest temperaments,
                                  Set<FriendlyWith> friendlyWith) {
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.temperaments = temperaments;
        this.friendlyWith = friendlyWith == null ? new HashSet<>() : friendlyWith;
    }
}

