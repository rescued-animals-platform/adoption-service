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

import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.PictureType;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class ImagePictureBuilder {

    private UUID animalUuid;
    private String name;
    private PictureType pictureType;
    private Image largeImage;
    private Image smallImage;

    public static ImagePictureBuilder random() {
        final ImagePictureBuilder imagePictureBuilder = new ImagePictureBuilder();
        imagePictureBuilder.animalUuid = UUID.randomUUID();
        imagePictureBuilder.name = randomAlphabetic(10);
        imagePictureBuilder.pictureType = getRandomPictureType();
        imagePictureBuilder.largeImage = ImageBuilder.random().build();
        imagePictureBuilder.smallImage = ImageBuilder.random().build();
        return imagePictureBuilder;
    }

    public ImagePictureBuilder withAnimalUuid(final UUID animalUuid) {
        this.animalUuid = animalUuid;
        return this;
    }

    public ImagePictureBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public ImagePictureBuilder withPictureType(final PictureType pictureType) {
        this.pictureType = pictureType;
        return this;
    }

    public ImagePictureBuilder withLargeImage(final Image largeImage) {
        this.largeImage = largeImage;
        return this;
    }

    public ImagePictureBuilder withSmallImage(final Image smallImage) {
        this.smallImage = smallImage;
        return this;
    }

    public ImagePicture build() {
        return new ImagePicture(animalUuid, name, pictureType, largeImage, smallImage);
    }
}
