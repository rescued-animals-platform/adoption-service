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

import ec.animal.adoption.domain.characteristics.temperaments.TemperamentsBuilder;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomFriendlyWith;
import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;

public class CharacteristicsBuilder {

    private UUID characteristicsId;
    private LocalDateTime registrationDate;
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

    public CharacteristicsBuilder withIdentifier(final UUID characteristicsId) {
        this.characteristicsId = characteristicsId;
        return this;
    }

    public CharacteristicsBuilder withRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
        return this;
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
        return new Characteristics(
                this.characteristicsId,
                this.registrationDate,
                this.size,
                this.physicalActivity,
                this.temperaments,
                this.friendlyWith
        );
    }
}
