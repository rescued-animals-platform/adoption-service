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

package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.FriendlyWith;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "friendly_with")
public class JpaFriendlyWith {

    @EmbeddedId
    private JpaFriendlyWithId jpaFriendlyWithId;

    private JpaFriendlyWith() {
        // Required by jpa
    }

    public JpaFriendlyWith(FriendlyWith friendlyWith, JpaCharacteristics jpaCharacteristics) {
        this();
        this.jpaFriendlyWithId = new JpaFriendlyWithId(friendlyWith.name(), jpaCharacteristics);
    }

    public FriendlyWith toFriendlyWith() {
        return FriendlyWith.valueOf(this.jpaFriendlyWithId.getFriendlyWith());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaFriendlyWith that = (JpaFriendlyWith) o;

        return jpaFriendlyWithId != null ? jpaFriendlyWithId.equals(that.jpaFriendlyWithId) : that.jpaFriendlyWithId == null;
    }

    @Override
    public int hashCode() {
        return jpaFriendlyWithId != null ? jpaFriendlyWithId.hashCode() : 0;
    }
}
