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
import ec.animal.adoption.domain.media.SupportedImageExtension;

import java.util.Random;

import static ec.animal.adoption.TestUtils.getRandomSupportedImageExtension;

public class ImageBuilder {

    private static final int ONE_MEGA_BYTE_IN_BYTES = 1048576;

    private String extension;
    private byte[] content;
    private long sizeInBytes;

    public static ImageBuilder empty() {
        return new ImageBuilder();
    }

    public static ImageBuilder random() {
        ImageBuilder imageBuilder = new ImageBuilder();
        SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        imageBuilder.extension = supportedImageExtension.getExtension();
        imageBuilder.content = supportedImageExtension.getStartingBytes();
        int sizeInBytes = new Random().nextInt(ONE_MEGA_BYTE_IN_BYTES);
        imageBuilder.sizeInBytes = sizeInBytes == 0 ? 1 : sizeInBytes;
        return imageBuilder;
    }

    public ImageBuilder withSupportedImageExtension(SupportedImageExtension supportedImageExtension) {
        this.extension = supportedImageExtension.getExtension();
        this.content = supportedImageExtension.getStartingBytes();
        return this;
    }

    public ImageBuilder withExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public ImageBuilder withContent(byte[] content) {
        this.content = content;
        return this;
    }

    public ImageBuilder withSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
        return this;
    }

    public Image build() {
        return new Image(extension, content, sizeInBytes);
    }
}
