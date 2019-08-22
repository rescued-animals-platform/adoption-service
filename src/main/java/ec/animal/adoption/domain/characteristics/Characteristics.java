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
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import ec.animal.adoption.validators.ValidTemperaments;

import javax.validation.constraints.NotNull;
import java.util.*;

@SuppressWarnings("PMD.DataClass")
public class Characteristics {

    @NotNull(message = "Size is required")
    @JsonProperty("size")
    private final Size size;

    @NotNull(message = "Physical activity is required")
    @JsonProperty("physicalActivity")
    private final PhysicalActivity physicalActivity;

    @ValidTemperaments
    @JsonProperty("temperaments")
    private final Temperaments temperaments;

    @JsonProperty("friendlyWith")
    private final Set<FriendlyWith> friendlyWith;

    @JsonCreator
    public Characteristics(
            @JsonProperty("size") final Size size,
            @JsonProperty("physicalActivity") final PhysicalActivity physicalActivity,
            @JsonProperty("temperaments") final Temperaments temperaments,
            @JsonProperty("friendlyWith") final FriendlyWith... friendlyWith
    ) {
        this.size = size;
        this.physicalActivity = physicalActivity;
        this.friendlyWith = new HashSet<>(Arrays.asList(friendlyWith));
        this.temperaments = temperaments;
    }

    public Size getSize() {
        return size;
    }

    public PhysicalActivity getPhysicalActivity() {
        return physicalActivity;
    }

    public Temperaments getTemperaments() {
        return temperaments;
    }

    public List<FriendlyWith> getFriendlyWith() {
        return new ArrayList<>(friendlyWith);
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Characteristics that = (Characteristics) o;

        if (size != that.size) return false;
        if (physicalActivity != that.physicalActivity) return false;
        if (temperaments != null ? !temperaments.equals(that.temperaments) : that.temperaments != null) return false;
        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = size != null ? size.hashCode() : 0;
        result = 31 * result + (physicalActivity != null ? physicalActivity.hashCode() : 0);
        result = 31 * result + (temperaments != null ? temperaments.hashCode() : 0);
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}
