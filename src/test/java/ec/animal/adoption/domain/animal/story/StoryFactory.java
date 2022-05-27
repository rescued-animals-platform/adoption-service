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

package ec.animal.adoption.domain.animal.story;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class StoryFactory {

    private UUID storyId;
    private LocalDateTime registrationDate;
    private String text;

    public static StoryFactory random() {
        StoryFactory storyFactory = new StoryFactory();
        storyFactory.withIdentifier(UUID.randomUUID());
        storyFactory.withRegistrationDate(LocalDateTime.now());
        storyFactory.text = randomAlphabetic(300);
        return storyFactory;
    }

    public StoryFactory withIdentifier(final UUID storyId) {
        this.storyId = storyId;
        return this;
    }

    public StoryFactory withRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public StoryFactory withText(final String text) {
        this.text = text;
        return this;
    }

    public Story build() {
        return new Story(this.storyId, this.registrationDate, this.text);
    }
}
