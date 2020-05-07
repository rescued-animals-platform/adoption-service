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

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import static ec.animal.adoption.TestUtils.getRandomSupportedImageExtension;

public class ImageBuilder {

    private static final int ONE_MEGA_BYTE_IN_BYTES = 1_048_576;

    private String extension;
    private byte[] content;
    private long sizeInBytes;

    public static ImageBuilder random() {
        final ImageBuilder imageBuilder = new ImageBuilder();
        final SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        imageBuilder.extension = supportedImageExtension.getExtension();

        final byte[] startingBytes = supportedImageExtension.getStartingBytes();
        final Random random = new Random();
        final byte[] randomContent = new byte[100];
        random.nextBytes(randomContent);
        final byte[] content = new byte[startingBytes.length + randomContent.length];
        final ByteBuffer byteBuffer = ByteBuffer.wrap(content);
        byteBuffer.put(startingBytes);
        byteBuffer.put(randomContent);
        imageBuilder.content = byteBuffer.array();

        final int sizeInBytes = new Random().nextInt(ONE_MEGA_BYTE_IN_BYTES);
        imageBuilder.sizeInBytes = sizeInBytes == 0 ? 1 : sizeInBytes;

        return imageBuilder;
    }

    public ImageBuilder withSupportedImageExtension(final SupportedImageExtension supportedImageExtension) {
        this.extension = supportedImageExtension.getExtension();
        this.content = supportedImageExtension.getStartingBytes();
        return this;
    }

    public ImageBuilder withExtension(final String extension) {
        this.extension = extension;
        return this;
    }

    public ImageBuilder withContent(final byte[] content) {
        this.content = Arrays.copyOf(content, content.length);
        return this;
    }

    public ImageBuilder withSizeInBytes(final long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
        return this;
    }

    public Image build() {
        return new Image(extension, content, sizeInBytes);
    }
}
