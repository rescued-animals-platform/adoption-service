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

package ec.animal.adoption.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Entity {

    @JsonProperty("id")
    protected final UUID identifier;

    @JsonProperty("registrationDate")
    protected final LocalDateTime registrationDate;

    protected Entity(final UUID identifier, final LocalDateTime registrationDate) {
        this.identifier = identifier;
        this.registrationDate = registrationDate;
    }

    protected Entity() {
        this.identifier = null;
        this.registrationDate = null;
    }

    public abstract UUID getIdentifier();

    public abstract LocalDateTime getRegistrationDate();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Entity entity = (Entity) o;

        if (identifier != null ? !identifier.equals(entity.identifier) : entity.identifier != null) {
            return false;
        }
        return registrationDate != null ? registrationDate.equals(entity.registrationDate) : entity.registrationDate == null;
    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        return result;
    }
}
