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

package ec.animal.adoption.domain.model.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import ec.animal.adoption.domain.model.utils.EnumUtils;

public enum StateName {
    LOOKING_FOR_HUMAN, ADOPTED, UNAVAILABLE;

    @JsonCreator
    private static StateName forValue(final String value) {
        return (StateName) EnumUtils.forValue(value)
                                    .apply(StateName.values())
                                    .orElseThrow(IllegalArgumentException::new);
    }
}
