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

package ec.animal.adoption.domain.model.characteristics;

import ec.animal.adoption.domain.model.Default;
import ec.animal.adoption.domain.model.Entity;
import ec.animal.adoption.domain.model.characteristics.temperaments.Temperaments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Characteristics extends Entity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Characteristics.class);

    private final Size size;
    private final PhysicalActivity physicalActivity;
    private final Temperaments temperaments;
    private final Set<FriendlyWith> friendlyWith;

    @Default
    public Characteristics(@NonNull final UUID identifier,
                           @NonNull final LocalDateTime registrationDate,
                           @NonNull final Size size,
                           @NonNull final PhysicalActivity physicalActivity,
                           @NonNull final Temperaments temperaments,
                           @NonNull final Set<FriendlyWith> friendlyWith) {
        super(identifier, registrationDate);
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

    public Characteristics updateWith(@NonNull final Characteristics characteristics) {
        if (this.equals(characteristics)) {
            return this;
        }

        UUID characteristicsId = this.getIdentifier();
        LocalDateTime registrationDate = this.getRegistrationDate();
        if (characteristicsId == null || registrationDate == null) {
            LOGGER.error("Error when trying to update characteristics that have no identifier or registration date");
            throw new IllegalArgumentException();
        }

        return new Characteristics(characteristicsId,
                                   registrationDate,
                                   characteristics.getSize(),
                                   characteristics.getPhysicalActivity(),
                                   characteristics.getTemperaments(),
                                   characteristics.getFriendlyWith());
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
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (physicalActivity != null ? physicalActivity.hashCode() : 0);
        result = 31 * result + (temperaments != null ? temperaments.hashCode() : 0);
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}

