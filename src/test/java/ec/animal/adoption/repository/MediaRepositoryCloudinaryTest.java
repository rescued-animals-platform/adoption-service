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

package ec.animal.adoption.repository;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import ec.animal.adoption.builders.ImagePictureBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.repository.exception.CloudinaryImageStorageException;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MediaRepositoryCloudinaryTest {

    @Mock
    private Cloudinary cloudinary;

    private MediaRepositoryCloudinary cloudinaryMediaStorageClient;

    @BeforeEach
    public void setUp() {
        cloudinaryMediaStorageClient = new MediaRepositoryCloudinary(cloudinary);
    }

    @Test
    public void shouldBeAnInstanceOfMediaStorageClient() {
        assertThat(cloudinaryMediaStorageClient, is(instanceOf(MediaRepository.class)));
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

        assertEquals(expectedPicture, storedPicture);
    }

    @Test
    public void shouldThrowImageStorageExceptionWhenStoringLargeImage() throws IOException {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(eq(imagePicture.getLargeImageContent()), anyMap()))
                .thenThrow(IOException.class);

        assertThrows(CloudinaryImageStorageException.class, () -> {
            cloudinaryMediaStorageClient.save(imagePicture);
        });
    }

    @Test
    public void shouldThrowImageStorageExceptionWhenStoringSmallImage() throws IOException {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(eq(imagePicture.getLargeImageContent()), anyMap()))
                .thenReturn(Map.of("url", "large-image-url"));
        when(uploader.upload(eq(imagePicture.getSmallImageContent()), anyMap()))
                .thenThrow(IOException.class);

        assertThrows(CloudinaryImageStorageException.class, () -> {
            cloudinaryMediaStorageClient.save(imagePicture);
        });
    }
}