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
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;

import static ec.animal.adoption.TestUtils.*;

public class CharacteristicsBuilder {

    private Size size;
    private PhysicalActivity physicalActivity;
    private boolean isTemperamentsSet = false;
    private Temperaments temperaments;
    private Sociability sociability;
    private Docility docility;
    private Balance balance;
    private FriendlyWith[] friendlyWith;

    public static CharacteristicsBuilder random() {
        CharacteristicsBuilder characteristicsBuilder = new CharacteristicsBuilder();
        characteristicsBuilder.size = getRandomSize();
        characteristicsBuilder.physicalActivity = getRandomPhysicalActivity();
        characteristicsBuilder.sociability = getRandomSociability();
        characteristicsBuilder.docility = getRandomDocility();
        characteristicsBuilder.balance = getRandomBalance();
        characteristicsBuilder.friendlyWith = new FriendlyWith[]{getRandomFriendlyWith()};
        return characteristicsBuilder;
    }

    public CharacteristicsBuilder withSize(Size size) {
        this.size = size;
        return this;
    }

    public CharacteristicsBuilder withPhysicalActivity(PhysicalActivity physicalActivity) {
        this.physicalActivity = physicalActivity;
        return this;
    }

    public CharacteristicsBuilder withSociability(Sociability sociability) {
        this.sociability = sociability;
        return this;
    }

    public CharacteristicsBuilder withDocility(Docility docility) {
        this.docility = docility;
        return this;
    }

    public CharacteristicsBuilder withBalance(Balance balance) {
        this.balance = balance;
        return this;
    }

    public CharacteristicsBuilder withTemperaments(Temperaments temperaments) {
        this.isTemperamentsSet = true;
        this.temperaments = temperaments;
        return this;
    }

    public CharacteristicsBuilder withFriendlyWith(FriendlyWith... friendlyWith) {
        this.friendlyWith = friendlyWith;
        return this;
    }

    public Characteristics build() {
        return new Characteristics(
                this.size,
                this.physicalActivity,
                isTemperamentsSet ? temperaments : new Temperaments(this.sociability, this.docility, this.balance),
                this.friendlyWith
        );
    }
}
