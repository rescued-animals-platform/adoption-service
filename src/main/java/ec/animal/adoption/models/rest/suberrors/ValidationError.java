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

package ec.animal.adoption.models.rest.suberrors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError implements ApiSubError {

    @JsonProperty("field")
    private final String field;

    @JsonProperty("message")
    private final String message;

    @JsonCreator
    public ValidationError(@JsonProperty("field") final String field, @JsonProperty("message") final String message) {
        this.field = field;
        this.message = message;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidationError that = (ValidationError) o;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
