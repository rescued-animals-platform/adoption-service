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

package ec.animal.adoption.api.model.characteristics.temperaments;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.api.validator.ValidTemperamentsRequest;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;

@ValidTemperamentsRequest
public class TemperamentsRequest {

    @JsonProperty("sociability")
    private final Sociability sociability;

    @JsonProperty("docility")
    private final Docility docility;

    @JsonProperty("balance")
    private final Balance balance;

    @JsonCreator
    public TemperamentsRequest(@JsonProperty("sociability") final Sociability sociability,
                               @JsonProperty("docility") final Docility docility,
                               @JsonProperty("balance") final Balance balance) {
        this.sociability = sociability;
        this.docility = docility;
        this.balance = balance;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return sociability == null && docility == null && balance == null;
    }

    public Temperaments toDomain() {
        return new Temperaments(this.sociability,
                                this.docility,
                                this.balance);
    }
}

