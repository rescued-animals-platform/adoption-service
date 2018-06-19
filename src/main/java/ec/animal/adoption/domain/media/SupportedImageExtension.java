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

import java.util.Arrays;
import java.util.Optional;

public enum SupportedImageExtension {
    JPEG(new byte[]{(byte) 0xff, (byte) 0xd8}, "jpeg"),
    JPG(new byte[]{(byte) 0xff, (byte) 0xd8}, "jpg"),
    PNG(new byte[]{
            (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a
    }, "png");

    private final String extension;
    private final byte[] startingBytes;

    SupportedImageExtension(byte[] startingBytes, String extension) {
        this.extension = extension;
        this.startingBytes = startingBytes;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getStartingBytes() {
        return startingBytes;
    }

    public static Optional<SupportedImageExtension> getMatchFor(String extension, byte[] content) {
        return Arrays.stream(SupportedImageExtension.values()).filter(s -> {
            byte[] startingBytesFromContent = Arrays.copyOf(content, s.startingBytes.length);
            return s.extension.equals(extension) && Arrays.equals(startingBytesFromContent, s.startingBytes);
        }).findFirst();
    }
}
