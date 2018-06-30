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

import java.util.UUID;

public class ImagePicture {

    private final UUID animalUuid;
    private final String name;
    private final PictureType pictureType;
    private final Image largeImage;
    private final Image smallImage;

    public ImagePicture(UUID animalUuid, String name, PictureType pictureType, Image largeImage, Image smallImage) {
        this.animalUuid = animalUuid;
        this.name = name;
        this.pictureType = pictureType;
        this.largeImage = largeImage;
        this.smallImage = smallImage;
    }

    public UUID getAnimalUuid() {
        return animalUuid;
    }

    public String getName() {
        return name;
    }

    public PictureType getPictureType() {
        return pictureType;
    }

    private String getLargeImageName() {
        return this.name + "_LARGE." + largeImage.getExtension();
    }

    public String getLargeImagePath() {
        return this.animalUuid + "/" + this.getLargeImageName();
    }

    public byte[] getLargeImageContent() {
        return this.largeImage.getContent();
    }

    private String getSmallImageName() {
        return this.name + "_SMALL." + smallImage.getExtension();
    }

    public String getSmallImagePath() {
        return this.animalUuid + "/" + this.getSmallImageName();
    }

    public byte[] getSmallImageContent() {
        return this.smallImage.getContent();
    }

    public boolean isValid() {
        return largeImage.isValid() && smallImage.isValid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImagePicture that = (ImagePicture) o;

        if (animalUuid != null ? !animalUuid.equals(that.animalUuid) : that.animalUuid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pictureType != that.pictureType) return false;
        if (largeImage != null ? !largeImage.equals(that.largeImage) : that.largeImage != null) return false;
        return smallImage != null ? smallImage.equals(that.smallImage) : that.smallImage == null;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pictureType != null ? pictureType.hashCode() : 0);
        result = 31 * result + (largeImage != null ? largeImage.hashCode() : 0);
        result = 31 * result + (smallImage != null ? smallImage.hashCode() : 0);
        return result;
    }
}
