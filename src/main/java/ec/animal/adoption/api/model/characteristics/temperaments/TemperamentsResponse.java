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
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.animal.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.animal.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.animal.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.animal.characteristics.temperaments.Temperaments;

public class TemperamentsResponse {

    @JsonProperty("sociability")
    private final String sociability;

    @JsonProperty("docility")
    private final String docility;

    @JsonProperty("balance")
    private final String balance;

    @JsonCreator
    private TemperamentsResponse(@JsonProperty("sociability") final String sociability,
                                 @JsonProperty("docility") final String docility,
                                 @JsonProperty("balance") final String balance) {
        this.sociability = sociability;
        this.docility = docility;
        this.balance = balance;
    }

    public static TemperamentsResponse from(final Temperaments temperaments) {
        return new TemperamentsResponse(
                temperaments.getSociability().map(Sociability::name).orElse(null),
                temperaments.getDocility().map(Docility::name).orElse(null),
                temperaments.getBalance().map(Balance::name).orElse(null)
        );
    }
}

