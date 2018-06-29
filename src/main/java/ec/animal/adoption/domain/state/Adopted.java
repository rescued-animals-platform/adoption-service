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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.helpers.DateTimeHelper;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Adopted implements State {

    private final LocalDateTime date;

    @JsonProperty("adoptionFormId")
    private final String adoptionFormId;


    @JsonCreator
    private Adopted(@JsonProperty("adoptionFormId") String adoptionFormId) {
        this.date = LocalDateTime.now();
        this.adoptionFormId = adoptionFormId;
    }

    public Adopted(LocalDateTime date, String adoptionFormId) {
        this.date = date;
        this.adoptionFormId = adoptionFormId;
    }

    @JsonProperty("date")
    private ZonedDateTime getDateInZonedDateTime() {
        return DateTimeHelper.getZonedDateTime(date);
    }

    @JsonProperty("adoptionFormId")
    public String getAdoptionFormId() {
        return adoptionFormId;
    }

    @JsonIgnore
    public LocalDateTime getDate() {
        return date;
    }
}
