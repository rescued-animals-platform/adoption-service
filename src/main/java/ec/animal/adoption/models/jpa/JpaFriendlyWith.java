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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity(name = "friendly_with")
public class JpaFriendlyWith implements Serializable {

    private transient static final long serialVersionUID = -412422759164428810L;

    @EmbeddedId
    private JpaFriendlyWithId jpaFriendlyWithId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "characteristics_id", nullable = false)
    @MapsId("characteristicsId")
    private JpaCharacteristics jpaCharacteristics;

    private JpaFriendlyWith() {
        // Required by jpa
    }

    public JpaFriendlyWith(final FriendlyWith friendlyWith, final JpaCharacteristics jpaCharacteristics) {
        this();
        this.jpaFriendlyWithId = new JpaFriendlyWithId(friendlyWith.name());
        this.jpaCharacteristics = jpaCharacteristics;
    }

    public FriendlyWith toFriendlyWith() {
        return FriendlyWith.valueOf(this.jpaFriendlyWithId.getFriendlyWith());
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaFriendlyWith that = (JpaFriendlyWith) o;

        if (jpaFriendlyWithId != null ? !jpaFriendlyWithId.equals(that.jpaFriendlyWithId) : that.jpaFriendlyWithId != null)
            return false;
        return jpaCharacteristics != null ? jpaCharacteristics.equals(that.jpaCharacteristics) : that.jpaCharacteristics == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = jpaFriendlyWithId != null ? jpaFriendlyWithId.hashCode() : 0;
        result = 31 * result + (jpaCharacteristics != null ? jpaCharacteristics.hashCode() : 0);
        return result;
    }
}
