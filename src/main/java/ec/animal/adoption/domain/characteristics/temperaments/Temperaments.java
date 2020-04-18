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

package ec.animal.adoption.domain.characteristics.temperaments;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Temperaments {

    @JsonProperty("sociability")
    private final Sociability sociability;

    @JsonProperty("docility")
    private final Docility docility;

    @JsonProperty("balance")
    private final Balance balance;

    @JsonCreator
    public Temperaments(
            @JsonProperty("sociability") final Sociability sociability,
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

    public Sociability getSociability() {
        return sociability;
    }

    public Docility getDocility() {
        return docility;
    }

    public Balance getBalance() {
        return balance;
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

        Temperaments that = (Temperaments) o;

        if (sociability != that.sociability) {
            return false;
        }
        if (docility != that.docility) {
            return false;
        }
        return balance == that.balance;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = sociability != null ? sociability.hashCode() : 0;
        result = 31 * result + (docility != null ? docility.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}
