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

package ec.animal.adoption.client.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import ec.animal.adoption.builders.ImagePictureBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.client.MediaStorageClient;
import ec.animal.adoption.domain.exception.ImageStorageException;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(MockitoJUnitRunner.class)
public class CloudinaryMediaStorageClientTest {

    @Mock
    private Cloudinary cloudinary;

    private CloudinaryMediaStorageClient cloudinaryMediaStorageClient;

    @Before
    public void setUp() {
        cloudinaryMediaStorageClient = new CloudinaryMediaStorageClient(cloudinary);
    }

    @Test
    public void shouldBeAnInstanceOfMediaStorageClient() {
        assertThat(cloudinaryMediaStorageClient, is(instanceOf(MediaStorageClient.class)));
    }

    @Test
    public void shouldStorePicture() throws IOException {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        String largeImageUrl = randomAlphabetic(10);
        String smallImageUrl = randomAlphabetic(10);
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(eq(imagePicture.getLargeImageContent()), anyMap()))
                .thenReturn(Map.of("url", largeImageUrl));
        when(uploader.upload(eq(imagePicture.getSmallImageContent()), anyMap()))
                .thenReturn(Map.of("url", smallImageUrl));

        LinkPicture expectedPicture = LinkPictureBuilder
                .random()
                .withName(imagePicture.getName())
                .withPictureType(imagePicture.getPictureType())
                .withLargeImageMediaLink(new MediaLink(largeImageUrl))
                .withSmallImageMediaLink(new MediaLink(smallImageUrl))
                .build();

        LinkPicture storedPicture = cloudinaryMediaStorageClient.save(imagePicture);

        assertReflectionEquals(expectedPicture, storedPicture);
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringLargeImage() throws IOException {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(eq(imagePicture.getLargeImageContent()), anyMap()))
                .thenThrow(IOException.class);

        cloudinaryMediaStorageClient.save(imagePicture);
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringSmallImage() throws IOException {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(eq(imagePicture.getLargeImageContent()), anyMap()))
                .thenReturn(Map.of("url", "large-image-url"));
        when(uploader.upload(eq(imagePicture.getSmallImageContent()), anyMap()))
                .thenThrow(IOException.class);

        cloudinaryMediaStorageClient.save(imagePicture);
    }
}