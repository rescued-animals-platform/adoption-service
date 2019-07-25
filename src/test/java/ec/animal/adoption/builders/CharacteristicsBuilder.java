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

package ec.animal.adoption.builders;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;

import java.util.Arrays;

import static ec.animal.adoption.TestUtils.*;

public class CharacteristicsBuilder {

    private Size size;
    private PhysicalActivity physicalActivity;
    private Temperaments temperaments;
    private FriendlyWith[] friendlyWith;

    public static CharacteristicsBuilder random() {
        final CharacteristicsBuilder characteristicsBuilder = new CharacteristicsBuilder();
        characteristicsBuilder.size = getRandomSize();
        characteristicsBuilder.physicalActivity = getRandomPhysicalActivity();
        characteristicsBuilder.temperaments = TemperamentsBuilder.random().build();
        characteristicsBuilder.friendlyWith = new FriendlyWith[]{getRandomFriendlyWith()};
        return characteristicsBuilder;
    }

    public CharacteristicsBuilder withSize(final Size size) {
        this.size = size;
        return this;
    }

    public CharacteristicsBuilder withPhysicalActivity(final PhysicalActivity physicalActivity) {
        this.physicalActivity = physicalActivity;
        return this;
    }

    public CharacteristicsBuilder withTemperaments(final Temperaments temperaments) {
        this.temperaments = temperaments;
        return this;
    }

    public CharacteristicsBuilder withFriendlyWith(final FriendlyWith... friendlyWith) {
        this.friendlyWith = Arrays.copyOf(friendlyWith, friendlyWith.length);
        return this;
    }

    public Characteristics build() {
        return new Characteristics(this.size, this.physicalActivity, this.temperaments, this.friendlyWith);
    }
}
