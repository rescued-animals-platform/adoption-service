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

package ec.animal.adoption.api.model.characteristics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.api.model.characteristics.temperaments.TemperamentsResponse;
import ec.animal.adoption.domain.characteristics.Characteristics;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("PMD")
public class CharacteristicsResponse {

    @JsonProperty("size")
    private final String size;

    @JsonProperty("physicalActivity")
    private final String physicalActivity;

    @JsonProperty("temperaments")
    private final TemperamentsResponse temperaments;

    @JsonProperty("friendlyWith")
    private final Set<String> friendlyWith;

    @JsonCreator
    private CharacteristicsResponse(@JsonProperty("size") final String size,
                                    @JsonProperty("physicalActivity") final String physicalActivity,
                                    @JsonProperty("temperaments") final TemperamentsResponse temperaments,
                                    @JsonProperty("friendlyWith") final Set<String> friendlyWith) {
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.temperaments = temperaments;
        this.friendlyWith = friendlyWith;
    }

    public static CharacteristicsResponse from(final Characteristics characteristics) {
        return new CharacteristicsResponse(characteristics.getSize().toString(),
                                           characteristics.getPhysicalActivity().toString(),
                                           TemperamentsResponse.from(characteristics.getTemperaments()),
                                           characteristics.getFriendlyWith().stream()
                                                          .map(Enum::toString)
                                                          .collect(Collectors.toSet()));
    }
}

