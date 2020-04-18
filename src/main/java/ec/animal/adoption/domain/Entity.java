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

package ec.animal.adoption.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class Entity {

    @JsonProperty("uuid")
    private final UUID uuid;

    @JsonProperty("registrationDate")
    private final LocalDateTime registrationDate;

    protected Entity(final UUID uuid, final LocalDateTime registrationDate) {
        this.uuid = uuid;
        this.registrationDate = registrationDate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Entity entity = (Entity) o;

        if (uuid != null ? !uuid.equals(entity.uuid) : entity.uuid != null) {
            return false;
        }
        return registrationDate != null ? registrationDate.equals(entity.registrationDate) : entity.registrationDate == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        return result;
    }
}
