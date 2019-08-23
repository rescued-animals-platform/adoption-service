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

package ec.animal.adoption.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties({"uuid", "registrationDate"})
public class Story extends Entity {

    @NotEmpty(message = "Story text is required")
    @JsonProperty("text")
    private final String text;

    @JsonCreator
    private Story(@JsonProperty("text") final String text) {
        super(null, null);
        this.text = text;
    }

    public Story(final UUID uuid, final LocalDateTime registrationDate, final String text) {
        super(uuid, registrationDate);
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Story story = (Story) o;

        return text != null ? text.equals(story.text) : story.text == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
