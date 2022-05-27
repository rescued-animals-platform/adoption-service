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

import static ec.animal.adoption.TestUtils.*;

public class TemperamentsFactory {

    private Sociability sociability;
    private Docility docility;
    private Balance balance;

    public static TemperamentsFactory random() {
        final TemperamentsFactory temperamentsFactory = new TemperamentsFactory();
        temperamentsFactory.sociability = getRandomSociability();
        temperamentsFactory.docility = getRandomDocility();
        temperamentsFactory.balance = getRandomBalance();
        return temperamentsFactory;
    }

    public static TemperamentsFactory empty() {
        return new TemperamentsFactory();
    }

    public TemperamentsFactory withSociability(final Sociability sociability) {
        this.sociability = sociability;
        return this;
    }

    public TemperamentsFactory withDocility(final Docility docility) {
        this.docility = docility;
        return this;
    }

    public TemperamentsFactory withBalance(final Balance balance) {
        this.balance = balance;
        return this;
    }

    public Temperaments build() {
        return new Temperaments(this.sociability, this.docility, this.balance);
    }
}
