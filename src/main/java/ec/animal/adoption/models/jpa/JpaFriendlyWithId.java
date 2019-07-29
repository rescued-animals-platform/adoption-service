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

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class JpaFriendlyWithId implements Serializable {

    private transient static final long serialVersionUID = -142436659179428820L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "characteristics_id", nullable = false)
    private JpaCharacteristics jpaCharacteristics;

    @NotNull
    private String friendlyWith;

    private JpaFriendlyWithId() {
        // Required by jpa
    }

    public JpaFriendlyWithId(final String friendlyWith, final JpaCharacteristics jpaCharacteristics) {
        this();
        this.friendlyWith = friendlyWith;
        this.jpaCharacteristics = jpaCharacteristics;
    }

    public @NotNull String getFriendlyWith() {
        return friendlyWith;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaFriendlyWithId that = (JpaFriendlyWithId) o;

        if (jpaCharacteristics != null ? !jpaCharacteristics.equals(that.jpaCharacteristics) : that.jpaCharacteristics != null)
            return false;
        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = jpaCharacteristics != null ? jpaCharacteristics.hashCode() : 0;
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}
