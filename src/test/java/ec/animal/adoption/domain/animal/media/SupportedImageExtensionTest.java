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

package ec.animal.adoption.domain.animal.media;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static ec.animal.adoption.domain.animal.media.SupportedImageExtension.JPEG;
import static ec.animal.adoption.domain.animal.media.SupportedImageExtension.JPG;
import static ec.animal.adoption.domain.animal.media.SupportedImageExtension.PNG;
import static ec.animal.adoption.domain.animal.media.SupportedImageExtension.getMatchFor;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SupportedImageExtensionTest {

    @Test
    void shouldReturnJpegFromExtensionAndContent() {
        byte[] jpegImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor("jpeg", jpegImageContent);

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(JPEG));
    }

    @Test
    void shouldReturnJpgFromExtensionAndContent() {
        byte[] jpgImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor("jpg", jpgImageContent);

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(JPG));
    }

    @Test
    void shouldReturnPngFromExtensionAndContent() {
        byte[] pngImageContent = {
                (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a
        };
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor("png", pngImageContent);

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(PNG));
    }

    @Test
    void shouldReturnEmptyFromNonMatchingExtension() {
        String nonMatchingExtension = randomAlphabetic(10);
        byte[] jpgImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor(nonMatchingExtension, jpgImageContent);

        assertThat(supportedImageExtension.isPresent(), is(false));
    }

    @Test
    void shouldReturnEmptyFromNonMatchingContent() {
        byte[] nonMatchingContent = {};
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor("png", nonMatchingContent);

        assertThat(supportedImageExtension.isPresent(), is(false));
    }
}