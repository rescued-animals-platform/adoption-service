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

package ec.animal.adoption.builders;

import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.PictureType;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class LinkPictureBuilder {

    private UUID animalUuid;
    private String name;
    private PictureType pictureType;
    private MediaLink largeImageMediaLink;
    private MediaLink smallImageMediaLink;

    public static LinkPictureBuilder random() {
        final LinkPictureBuilder linkPictureBuilder = new LinkPictureBuilder();
        linkPictureBuilder.animalUuid = UUID.randomUUID();
        linkPictureBuilder.name = randomAlphabetic(10);
        linkPictureBuilder.pictureType = getRandomPictureType();
        linkPictureBuilder.largeImageMediaLink = new MediaLink(randomAlphabetic(30));
        linkPictureBuilder.smallImageMediaLink = new MediaLink(randomAlphabetic(30));
        return linkPictureBuilder;
    }

    public LinkPictureBuilder withAnimalUuid(final UUID animalUuid) {
        this.animalUuid = animalUuid;
        return this;
    }

    public LinkPictureBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public LinkPictureBuilder withPictureType(final PictureType pictureType) {
        this.pictureType = pictureType;
        return this;
    }

    public LinkPictureBuilder withLargeImageMediaLink(final MediaLink largeImageMediaLink) {
        this.largeImageMediaLink = largeImageMediaLink;
        return this;
    }

    public LinkPictureBuilder withSmallImageMediaLink(final MediaLink smallImageMediaLink) {
        this.smallImageMediaLink = smallImageMediaLink;
        return this;
    }

    public LinkPicture build() {
        return new LinkPicture(
                this.animalUuid, this.name, this.pictureType, this.largeImageMediaLink, this.smallImageMediaLink
        );
    }
}
