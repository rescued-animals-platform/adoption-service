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

package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "temperaments")
public class JpaTemperaments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("PMD.ShortVariable")
    private Long id;

    private String sociability;

    private String docility;

    private String balance;


    private JpaTemperaments() {
        // Required by jpa
    }

    public JpaTemperaments(final Temperaments temperaments) {
        this();
        this.sociability = temperaments.getSociability() == null ? null : temperaments.getSociability().name();
        this.docility = temperaments.getDocility() == null ? null : temperaments.getDocility().name();
        this.balance = temperaments.getBalance() == null ? null : temperaments.getBalance().name();
    }

    public Temperaments toTemperaments() {
        return new Temperaments(
                this.sociability == null ? null : Sociability.valueOf(sociability),
                this.docility == null ? null : Docility.valueOf(this.docility),
                this.balance == null ? null : Balance.valueOf(this.balance)
        );
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaTemperaments that = (JpaTemperaments) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (sociability != null ? !sociability.equals(that.sociability) : that.sociability != null) return false;
        if (docility != null ? !docility.equals(that.docility) : that.docility != null) return false;
        return balance != null ? balance.equals(that.balance) : that.balance == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sociability != null ? sociability.hashCode() : 0);
        result = 31 * result + (docility != null ? docility.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}
