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

import ec.animal.adoption.domain.Entity;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("PMD.DataClass")
public class Characteristics extends Entity {

    private final Size size;
    private final PhysicalActivity physicalActivity;
    private final Temperaments temperaments;
    private final Set<FriendlyWith> friendlyWith;

    public Characteristics(@NonNull final UUID characteristicsId,
                           @NonNull final LocalDateTime registrationDate,
                           @NonNull final Size size,
                           @NonNull final PhysicalActivity physicalActivity,
                           @NonNull final Temperaments temperaments,
                           @NonNull final Set<FriendlyWith> friendlyWith) {
        super(characteristicsId, registrationDate);
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.friendlyWith = friendlyWith;
        this.temperaments = temperaments;
    }

    public Characteristics(@NonNull final Size size,
                           @NonNull final PhysicalActivity physicalActivity,
                           @NonNull final Temperaments temperaments,
                           @NonNull final Set<FriendlyWith> friendlyWith) {
        super();
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.friendlyWith = friendlyWith;
        this.temperaments = temperaments;
    }

    @NonNull
    public Size getSize() {
        return size;
    }

    @NonNull
    public PhysicalActivity getPhysicalActivity() {
        return physicalActivity;
    }

    @NonNull
    public Temperaments getTemperaments() {
        return temperaments;
    }

    @NonNull
    public Set<FriendlyWith> getFriendlyWith() {
        return new HashSet<>(friendlyWith);
    }

    @Override
    @Nullable
    public UUID getIdentifier() {
        return super.identifier;
    }

    @Override
    @Nullable
    public LocalDateTime getRegistrationDate() {
        return super.registrationDate;
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
        if (!super.equals(o)) {
            return false;
        }

        Characteristics that = (Characteristics) o;

        if (size != that.size) {
            return false;
        }
        if (physicalActivity != that.physicalActivity) {
            return false;
        }
        if (temperaments != null ? !temperaments.equals(that.temperaments) : that.temperaments != null) {
            return false;
        }
        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (physicalActivity != null ? physicalActivity.hashCode() : 0);
        result = 31 * result + (temperaments != null ? temperaments.hashCode() : 0);
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}

