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

package ec.animal.adoption.domain.animal.characteristics.temperaments;

import org.springframework.lang.Nullable;

import java.util.Optional;

public class Temperaments {

    private final Sociability sociability;
    private final Docility docility;
    private final Balance balance;

    public Temperaments(@Nullable final Sociability sociability,
                        @Nullable final Docility docility,
                        @Nullable final Balance balance) {
        this.sociability = sociability;
        this.docility = docility;
        this.balance = balance;
    }

    public Optional<Sociability> getSociability() {
        return Optional.ofNullable(sociability);
    }

    public Optional<Docility> getDocility() {
        return Optional.ofNullable(docility);
    }

    public Optional<Balance> getBalance() {
        return Optional.ofNullable(balance);
    }

    @Override
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
    public int hashCode() {
        int result = sociability != null ? sociability.hashCode() : 0;
        result = 31 * result + (docility != null ? docility.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}
