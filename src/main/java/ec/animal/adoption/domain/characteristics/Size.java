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

package ec.animal.adoption.domain.characteristics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.StringUtils;

import java.util.stream.Stream;

public enum Size {
    TINY("Tiny"), SMALL("Small"), MEDIUM("Medium"), BIG("Big"), OUTSIZE("Outsize");

    private final String name;

    Size(final String name) {
        this.name = name;
    }

    @JsonCreator
    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private static Size forValue(final String value) {
        String normalizedValue = StringUtils.trimToEmpty(value);
        try {
            return Size.valueOf(normalizedValue);
        } catch (IllegalArgumentException ex) {
            return Stream.of(Size.values())
                         .filter(e -> e.getName().equals(normalizedValue))
                         .findFirst()
                         .orElseThrow(() -> ex);
        }
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
