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

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static ec.animal.adoption.domain.media.SupportedImageExtension.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SupportedImageExtensionTest {

    @Test
    public void shouldReturnJpegFromExtensionAndContent() {
        byte[] jpegImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor("jpeg", jpegImageContent);

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(JPEG));
    }

    @Test
    public void shouldReturnJpgFromExtensionAndContent() {
        byte[] jpgImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor("jpg", jpgImageContent);

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(JPG));
    }

    @Test
    public void shouldReturnPngFromExtensionAndContent() {
        byte[] pngImageContent = {
                (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a
        };
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor("png", pngImageContent);

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(PNG));
    }

    @Test
    public void shouldReturnEmptyFromUnmatchingExtension() {
        String unmatchingExtension = randomAlphabetic(10);
        byte[] jpgImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor(unmatchingExtension, jpgImageContent);

        assertThat(supportedImageExtension.isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyFromUnmatchingContent() {
        byte[] unmatchingContent = {};
        Optional<SupportedImageExtension> supportedImageExtension = getMatchFor("png", unmatchingContent);

        assertThat(supportedImageExtension.isPresent(), is(false));
    }
}