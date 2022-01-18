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

package ec.animal.adoption.api.model.characteristics;

import ec.animal.adoption.api.model.characteristics.temperaments.TemperamentsRequest;
import ec.animal.adoption.api.model.characteristics.temperaments.TemperamentsRequestBuilder;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;

import java.util.Arrays;

import static ec.animal.adoption.TestUtils.*;

public class CharacteristicsRequestBuilder {

    private Size size;
    private PhysicalActivity physicalActivity;
    private TemperamentsRequest temperamentsRequest;
    private FriendlyWith[] friendlyWith;

    public static CharacteristicsRequestBuilder random() {
        final CharacteristicsRequestBuilder characteristicsBuilder = new CharacteristicsRequestBuilder();
        characteristicsBuilder.size = getRandomSize();
        characteristicsBuilder.physicalActivity = getRandomPhysicalActivity();
        characteristicsBuilder.temperamentsRequest = TemperamentsRequestBuilder.random().build();
        characteristicsBuilder.friendlyWith = new FriendlyWith[]{getRandomFriendlyWith()};
        return characteristicsBuilder;
    }

    public CharacteristicsRequestBuilder withSize(final Size size) {
        this.size = size;
        return this;
    }

    public CharacteristicsRequestBuilder withPhysicalActivity(final PhysicalActivity physicalActivity) {
        this.physicalActivity = physicalActivity;
        return this;
    }

    public CharacteristicsRequestBuilder withTemperaments(final TemperamentsRequest temperamentsRequest) {
        this.temperamentsRequest = temperamentsRequest;
        return this;
    }

    public CharacteristicsRequestBuilder withFriendlyWith(final FriendlyWith... friendlyWith) {
        this.friendlyWith = Arrays.copyOf(friendlyWith, friendlyWith.length);
        return this;
    }

    public CharacteristicsRequest build() {
        return new CharacteristicsRequest(this.size,
                                          this.physicalActivity,
                                          this.temperamentsRequest,
                                          this.friendlyWith);
    }
}
