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

public class ImagePicture {

    private final String name;
    private final PictureType pictureType;
    private final Image largeImage;
    private final Image smallImage;

    public ImagePicture(final String name,
                        final PictureType pictureType,
                        final Image largeImage,
                        final Image smallImage) {
        this.name = name;
        this.pictureType = pictureType;
        this.largeImage = largeImage;
        this.smallImage = smallImage;
    }

    public String getName() {
        return name;
    }

    public PictureType getPictureType() {
        return pictureType;
    }

    public byte[] getLargeImageContent() {
        return this.largeImage.getContent();
    }

    public byte[] getSmallImageContent() {
        return this.smallImage.getContent();
    }

    public boolean isValid() {
        return largeImage.isValid() && smallImage.isValid();
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

        ImagePicture that = (ImagePicture) o;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (pictureType != that.pictureType) {
            return false;
        }
        if (largeImage != null ? !largeImage.equals(that.largeImage) : that.largeImage != null) {
            return false;
        }
        return smallImage != null ? smallImage.equals(that.smallImage) : that.smallImage == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (pictureType != null ? pictureType.hashCode() : 0);
        result = 31 * result + (largeImage != null ? largeImage.hashCode() : 0);
        result = 31 * result + (smallImage != null ? smallImage.hashCode() : 0);
        return result;
    }
}
