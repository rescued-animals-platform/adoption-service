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

package ec.animal.adoption.domain.animal.characteristics;

import ec.animal.adoption.domain.animal.characteristics.temperaments.Temperaments;
import ec.animal.adoption.domain.animal.characteristics.temperaments.TemperamentsFactory;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomFriendlyWith;
import static ec.animal.adoption.TestUtils.getRandomPhysicalActivity;
import static ec.animal.adoption.TestUtils.getRandomSize;

public class CharacteristicsFactory {

    private UUID characteristicsId;
    private LocalDateTime registrationDate;
    private Size size;
    private PhysicalActivity physicalActivity;
    private Temperaments temperaments;
    private Set<FriendlyWith> friendlyWith;

    public static CharacteristicsFactory random() {
        final CharacteristicsFactory characteristicsFactory = new CharacteristicsFactory();
        characteristicsFactory.characteristicsId = UUID.randomUUID();
        characteristicsFactory.registrationDate = LocalDateTime.now();
        characteristicsFactory.size = getRandomSize();
        characteristicsFactory.physicalActivity = getRandomPhysicalActivity();
        characteristicsFactory.temperaments = TemperamentsFactory.random().build();
        characteristicsFactory.friendlyWith = Set.of(getRandomFriendlyWith());
        return characteristicsFactory;
    }

    public CharacteristicsFactory withIdentifier(final UUID characteristicsId) {
        this.characteristicsId = characteristicsId;
        return this;
    }

    public CharacteristicsFactory withRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public CharacteristicsFactory withSize(final Size size) {
        this.size = size;
        return this;
    }

    public CharacteristicsFactory withPhysicalActivity(final PhysicalActivity physicalActivity) {
        this.physicalActivity = physicalActivity;
        return this;
    }

    public CharacteristicsFactory withTemperaments(final Temperaments temperaments) {
        this.temperaments = temperaments;
        return this;
    }

    public CharacteristicsFactory withFriendlyWith(final FriendlyWith... friendlyWith) {
        this.friendlyWith = Set.of(friendlyWith);
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
