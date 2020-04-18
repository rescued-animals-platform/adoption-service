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

import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;

import static ec.animal.adoption.TestUtils.getRandomBalance;
import static ec.animal.adoption.TestUtils.getRandomDocility;
import static ec.animal.adoption.TestUtils.getRandomSociability;

public class TemperamentsBuilder {

    private Sociability sociability;
    private Docility docility;
    private Balance balance;

    public static TemperamentsBuilder random() {
        final TemperamentsBuilder temperamentsBuilder = new TemperamentsBuilder();
        temperamentsBuilder.sociability = getRandomSociability();
        temperamentsBuilder.docility = getRandomDocility();
        temperamentsBuilder.balance = getRandomBalance();
        return temperamentsBuilder;
    }

    public static TemperamentsBuilder empty() {
        return new TemperamentsBuilder();
    }

    public TemperamentsBuilder withSociability(final Sociability sociability) {
        this.sociability = sociability;
        return this;
    }

    public TemperamentsBuilder withDocility(final Docility docility) {
        this.docility = docility;
        return this;
    }

    public TemperamentsBuilder withBalance(final Balance balance) {
        this.balance = balance;
        return this;
    }

    public Temperaments build() {
        return new Temperaments(this.sociability, this.docility, this.balance);
    }
}
