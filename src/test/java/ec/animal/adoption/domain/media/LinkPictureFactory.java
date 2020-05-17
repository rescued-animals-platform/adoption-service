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

import java.time.LocalDateTime;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class LinkPictureFactory {

    private UUID linkPictureId;
    private LocalDateTime registrationDate;
    private String name;
    private PictureType pictureType;
    private MediaLink largeImageMediaLink;
    private MediaLink smallImageMediaLink;

    public static LinkPictureFactory random() {
        LinkPictureFactory linkPictureFactory = new LinkPictureFactory();
        linkPictureFactory.name = randomAlphabetic(10);
        linkPictureFactory.pictureType = getRandomPictureType();
        linkPictureFactory.largeImageMediaLink = new MediaLink(randomAlphabetic(30));
        linkPictureFactory.smallImageMediaLink = new MediaLink(randomAlphabetic(30));
        return linkPictureFactory;
    }

    public LinkPictureFactory withIdentifier(final UUID linkPictureId) {
        this.linkPictureId = linkPictureId;
        return this;
    }

    public LinkPictureFactory withRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public LinkPictureFactory withName(final String name) {
        this.name = name;
        return this;
    }

    public LinkPictureFactory withPictureType(final PictureType pictureType) {
        this.pictureType = pictureType;
        return this;
    }

    public LinkPictureFactory withLargeImageMediaLink(final MediaLink largeImageMediaLink) {
        this.largeImageMediaLink = largeImageMediaLink;
        return this;
    }

    public LinkPictureFactory withSmallImageMediaLink(final MediaLink smallImageMediaLink) {
        this.smallImageMediaLink = smallImageMediaLink;
        return this;
    }

    public LinkPicture build() {
        return new LinkPicture(
                this.linkPictureId,
                this.registrationDate,
                this.name,
                this.pictureType,
                this.largeImageMediaLink,
                this.smallImageMediaLink
        );
    }
}
