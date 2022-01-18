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

package ec.animal.adoption.domain.story;

import ec.animal.adoption.domain.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class Story extends Entity {

    private final static Logger LOGGER = LoggerFactory.getLogger(Story.class);

    private final String text;

    public Story(@NonNull final String text) {
        super();
        this.text = text;
    }

    public Story(@NonNull final UUID storyId,
                 @NonNull final LocalDateTime registrationDate,
                 @NonNull final String text) {
        super(storyId, registrationDate);
        this.text = text;
    }

    public Story updateWith(@NonNull final Story story) {
        if (this.equals(story)) {
            return this;
        }

        UUID storyId = this.getIdentifier();
        LocalDateTime registrationDate = this.getRegistrationDate();
        if (storyId == null || registrationDate == null) {
            LOGGER.error("Error when trying to update a story that has no identifier or registration date");
            throw new IllegalArgumentException();
        }

        return new Story(storyId, registrationDate, story.getText());
    }

    @NonNull
    public String getText() {
        return this.text;
    }

    @Override
    @Nullable
    public UUID getIdentifier() {
        return super.identifier;
    }

    @Override
    @Nullable
    public LocalDateTime getRegistrationDate() {
        return super.registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Story story = (Story) o;

        return text != null ? text.equals(story.text) : story.text == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
