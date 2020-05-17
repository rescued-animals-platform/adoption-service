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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImagePictureTest {

    @Test
    public void shouldCreateAnImagePicture() {
        String name = randomAlphabetic(10);
        PictureType pictureType = getRandomPictureType();
        ImagePicture imagePicture = ImagePictureFactory.random()
                                                       .withName(name)
                                                       .withPictureType(pictureType)
                                                       .build();

        assertThat(imagePicture.getName(), is(name));
        assertThat(imagePicture.getPictureType(), is(pictureType));
    }

    @Test
    public void shouldReturnLargeImageContent() {
        Image largeImage = mock(Image.class);
        byte[] content = {};
        when(largeImage.getContent()).thenReturn(content);
        ImagePicture imagePicture = ImagePictureFactory.random().withLargeImage(largeImage).build();

        assertThat(imagePicture.getLargeImageContent(), is(content));
    }

    @Test
    public void shouldReturnSmallImageContent() {
        Image smallImage = mock(Image.class);
        byte[] content = {};
        when(smallImage.getContent()).thenReturn(content);
        ImagePicture imagePicture = ImagePictureFactory.random().withSmallImage(smallImage).build();

        assertThat(imagePicture.getSmallImageContent(), is(content));
    }

    @Test
    public void shouldReturnTrueForValidImagePicture() {
        ImagePicture imagePicture = ImagePictureFactory.random().build();

        assertThat(imagePicture.isValid(), is(true));
    }

    @Test
    public void shouldReturnFalseWhenPictureImageHasAnInvalidLargeImage() {
        Image invalidLargeImage = mock(Image.class);
        when(invalidLargeImage.isValid()).thenReturn(false);
        ImagePicture imagePicture = ImagePictureFactory.random().withLargeImage(invalidLargeImage).build();

        assertThat(imagePicture.isValid(), is(false));
    }

    @Test
    public void shouldReturnFalseWhenPictureImageHasAnInvalidSmallImage() {
        Image invalidSmallImage = mock(Image.class);
        when(invalidSmallImage.isValid()).thenReturn(false);
        ImagePicture imagePicture = ImagePictureFactory.random().withSmallImage(invalidSmallImage).build();

        assertThat(imagePicture.isValid(), is(false));
    }

    @Test
    public void shouldReturnFalseWhenBothLargeAndSmallImageInsidePictureImageAreInvalid() {
        Image invalidImage = mock(Image.class);
        when(invalidImage.isValid()).thenReturn(false);
        ImagePicture imagePicture = ImagePictureFactory.random().withLargeImage(invalidImage).
                withSmallImage(invalidImage).build();

        assertThat(imagePicture.isValid(), is(false));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(ImagePicture.class).usingGetClass().verify();
    }
}