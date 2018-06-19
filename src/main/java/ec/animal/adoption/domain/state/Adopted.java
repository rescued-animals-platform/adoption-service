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

package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Adopted implements State {
    @JsonProperty("adoptionDate")
    private final LocalDate adoptionDate;

    @JsonProperty("adoptionFormId")
    private final String adoptionFormId;

    @JsonCreator
    public Adopted(
            @JsonProperty("adoptionDate") LocalDate adoptionDate,
            @JsonProperty("adoptionFormId") String adoptionFormId
    ) {
        this.adoptionDate = adoptionDate;
        this.adoptionFormId = adoptionFormId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adopted adopted = (Adopted) o;

        if (adoptionDate != null ? !adoptionDate.equals(adopted.adoptionDate) : adopted.adoptionDate != null)
            return false;
        return adoptionFormId != null ? adoptionFormId.equals(adopted.adoptionFormId) : adopted.adoptionFormId == null;
    }

    @Override
    public int hashCode() {
        int result = adoptionDate != null ? adoptionDate.hashCode() : 0;
        result = 31 * result + (adoptionFormId != null ? adoptionFormId.hashCode() : 0);
        return result;
    }
}
