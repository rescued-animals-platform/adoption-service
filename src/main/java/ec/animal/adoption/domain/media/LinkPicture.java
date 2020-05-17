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

import ec.animal.adoption.domain.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class LinkPicture extends Entity {

    private final static Logger LOGGER = LoggerFactory.getLogger(LinkPicture.class);

    private final String name;
    private final PictureType pictureType;
    private final MediaLink largeImageMediaLink;
    private final MediaLink smallImageMediaLink;

    public LinkPicture(final String name,
                       final PictureType pictureType,
                       final MediaLink largeImageMediaLink,
                       final MediaLink smallImageMediaLink) {
        super();
        this.name = name;
        this.pictureType = pictureType;
        this.largeImageMediaLink = largeImageMediaLink;
        this.smallImageMediaLink = smallImageMediaLink;
    }

    public LinkPicture(@NonNull final UUID linkPictureId,
                       @NonNull final LocalDateTime registrationDate,
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

    public LinkPicture updateWith(@NonNull final LinkPicture linkPicture) {
        if (this.equals(linkPicture)) {
            return this;
        }

        UUID linkPictureId = this.getIdentifier();
        LocalDateTime registrationDate = this.getRegistrationDate();
        if (linkPictureId == null || registrationDate == null) {
            LOGGER.error("Error when trying to update a link picture that has no identifier or registration date");
            throw new IllegalArgumentException();
        }

        return new LinkPicture(linkPictureId,
                               registrationDate,
                               linkPicture.getName(),
                               linkPicture.getPictureType(),
                               linkPicture.largeImageMediaLink,
                               linkPicture.smallImageMediaLink);
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

    public String getLargeImagePublicId() {
        return largeImageMediaLink.getPublicId();
    }

    public String getLargeImageUrl() {
        return largeImageMediaLink.getUrl();
    }

    public String getSmallImagePublicId() {
        return smallImageMediaLink.getPublicId();
    }

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
