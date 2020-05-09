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

package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.Entity;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties({"id", "registrationDate"})
public class LinkPicture extends Entity {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("pictureType")
    private final PictureType pictureType;

    @JsonProperty("largeImageMediaLink")
    private final MediaLink largeImageMediaLink;

    @JsonProperty("smallImageMediaLink")
    private final MediaLink smallImageMediaLink;

    @JsonCreator
    public LinkPicture(@JsonProperty("name") final String name,
                       @JsonProperty("pictureType") final PictureType pictureType,
                       @JsonProperty("largeImageMediaLink") final MediaLink largeImageMediaLink,
                       @JsonProperty("smallImageMediaLink") final MediaLink smallImageMediaLink) {
        super();
        this.name = name;
        this.pictureType = pictureType;
        this.largeImageMediaLink = largeImageMediaLink;
        this.smallImageMediaLink = smallImageMediaLink;
    }

    public LinkPicture(final UUID linkPictureId,
                       final LocalDateTime registrationDate,
                       final String name,
                       final PictureType pictureType,
                       final MediaLink largeImageMediaLink,
                       final MediaLink smallImageMediaLink) {
        super(linkPictureId, registrationDate);
        this.name = name;
        this.pictureType = pictureType;
        this.largeImageMediaLink = largeImageMediaLink;
        this.smallImageMediaLink = smallImageMediaLink;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return PictureType.PRIMARY.equals(pictureType);
    }

    public PictureType getPictureType() {
        return pictureType;
    }

    @JsonIgnore
    public String getLargeImageUrl() {
        return largeImageMediaLink.getUrl();
    }

    @JsonIgnore
    public String getSmallImageUrl() {
        return smallImageMediaLink.getUrl();
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
    @SuppressWarnings("PMD")
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

        LinkPicture that = (LinkPicture) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (pictureType != that.pictureType) {
            return false;
        }
        if (largeImageMediaLink != null ? !largeImageMediaLink.equals(that.largeImageMediaLink) : that.largeImageMediaLink != null) {
            return false;
        }
        return smallImageMediaLink != null ? smallImageMediaLink.equals(that.smallImageMediaLink) : that.smallImageMediaLink == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pictureType != null ? pictureType.hashCode() : 0);
        result = 31 * result + (largeImageMediaLink != null ? largeImageMediaLink.hashCode() : 0);
        result = 31 * result + (smallImageMediaLink != null ? smallImageMediaLink.hashCode() : 0);
        return result;
    }
}
