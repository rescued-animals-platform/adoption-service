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

package ec.animal.adoption.domain.model.media;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static ec.animal.adoption.TestUtils.getRandomSupportedImageExtension;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ImageTest {

    @Test
    void shouldCreateAnImage() {
        SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        long sizeInBytes = new Random().nextInt(100) + 1;
        Image image = ImageFactory.random().withSupportedImageExtension(supportedImageExtension).
                withSizeInBytes(sizeInBytes).build();

        assertThat(image.getExtension(), is(supportedImageExtension.getExtension()));
        assertThat(image.getContent(), is(supportedImageExtension.getStartingBytes()));
        assertThat(image.getSizeInBytes(), is(sizeInBytes));
    }

    @Test
    void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Image.class).usingGetClass().verify();
    }

    @Test
    void shouldBeValidForValidImageContentAndExtensionAndSizeBetweenZeroAndOneMb() {
        Image image = ImageFactory.random().build();

        assertThat(image.isValid(), is(true));
    }

    @Test
    void shouldBeValidForValidImageContentAndExtensionAndSizeEqualToOneMb() {
        int oneMegaByteInBytes = 1_048_576;
        Image image = ImageFactory.random().withSizeInBytes(oneMegaByteInBytes).build();

        assertThat(image.isValid(), is(true));
    }

    @Test
    void shouldBeInvalidForSizeGreaterThanOneMb() {
        int oneMegaByteInBytes = 1_048_576;
        int size = oneMegaByteInBytes + 1 + new Random().nextInt(100);
        Image image = ImageFactory.random().withSizeInBytes(size).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    void shouldBeInvalidForSizeEqualsToZero() {
        Image image = ImageFactory.random().withSizeInBytes(0).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    void shouldBeInvalidForSizeLessThanZero() {
        int size = -5000;
        Image image = ImageFactory.random().withSizeInBytes(size).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    void shouldBeInvalidWithRightExtensionInFilenameButWrongImageTypeInContent() {
        byte[] invalidContent = {};
        Image image = ImageFactory.random().withContent(invalidContent).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    void shouldBeInvalidWithRightImageTypeInContentButWrongExtensionInFilename() {
        String wrongExtension = randomAlphabetic(10);
        Image image = ImageFactory.random().withExtension(wrongExtension).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    void shouldBeInvalidWithWrongImageTypeInContentAndWrongExtensionInFilename() {
        String wrongExtension = randomAlphabetic(10);
        byte[] invalidContent = {};
        Image image = ImageFactory.random().withExtension(wrongExtension).withContent(invalidContent).build();

        assertThat(image.isValid(), is(false));
    }
}