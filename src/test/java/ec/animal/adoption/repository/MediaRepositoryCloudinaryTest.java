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
import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import ec.animal.adoption.repository.exception.CloudinaryImageStorageException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaRepositoryCloudinaryTest {

    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    private Organization organization;
    private Map<String, String> cloudinarySaveOptions;
    private Map<String, String> cloudinaryDeleteOptions;
    private MediaRepositoryCloudinary cloudinaryMediaStorageClient;

    @BeforeEach
    public void setUp() {
        organization = OrganizationFactory.random().build();
        cloudinarySaveOptions = Map.of("folder", organization.getOrganizationId().toString());
        cloudinaryDeleteOptions = Map.of("invalidate", "true");

        cloudinaryMediaStorageClient = new MediaRepositoryCloudinary(cloudinary);
    }

    @Test
    void shouldBeAnInstanceOfMediaStorageClient() {
        assertThat(cloudinaryMediaStorageClient, is(instanceOf(MediaRepository.class)));
    }

    @Test
    void shouldSavePicture() throws IOException {
        ImagePicture imagePicture = ImagePictureFactory.random().build();
        String largeImageUrl = randomAlphabetic(10);
        String largeImagePublicId = randomAlphabetic(10);
        String smallImageUrl = randomAlphabetic(10);
        String smallImagePublicId = randomAlphabetic(10);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(imagePicture.getLargeImageContent(), cloudinarySaveOptions))
                .thenReturn(Map.of(URL, largeImageUrl, PUBLIC_ID, largeImagePublicId));
        when(uploader.upload(imagePicture.getSmallImageContent(), cloudinarySaveOptions))
                .thenReturn(Map.of(URL, smallImageUrl, PUBLIC_ID, smallImagePublicId));
        LinkPicture expectedPicture = new LinkPicture(imagePicture.getName(),
                                                      imagePicture.getPictureType(),
                                                      new MediaLink(largeImagePublicId, largeImageUrl),
                                                      new MediaLink(smallImagePublicId, smallImageUrl));

        LinkPicture savedPicture = cloudinaryMediaStorageClient.save(imagePicture, organization);

        assertEquals(expectedPicture, savedPicture);
    }

    @Test
    void shouldThrowImageStorageExceptionWhenStoringLargeImage() throws IOException {
        ImagePicture imagePicture = ImagePictureFactory.random().build();
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(imagePicture.getLargeImageContent(), cloudinarySaveOptions))
                .thenThrow(IOException.class);

        assertThrows(CloudinaryImageStorageException.class, () -> {
            cloudinaryMediaStorageClient.save(imagePicture, organization);
        });
    }

    @Test
    void shouldThrowImageStorageExceptionWhenStoringSmallImage() throws IOException {
        ImagePicture imagePicture = ImagePictureFactory.random().build();
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(imagePicture.getLargeImageContent(), cloudinarySaveOptions))
                .thenReturn(Map.of(URL, "large-image-url", PUBLIC_ID, "large-image-public-id"));
        when(uploader.upload(imagePicture.getSmallImageContent(), cloudinarySaveOptions))
                .thenThrow(IOException.class);

        assertThrows(CloudinaryImageStorageException.class, () -> {
            cloudinaryMediaStorageClient.save(imagePicture, organization);
        });
    }

    @Test
    void shouldDeleteExistingLinkPicture() throws IOException {
        LinkPicture existingLinkPicture = LinkPictureFactory.random().build();
        when(cloudinary.uploader()).thenReturn(uploader);

        cloudinaryMediaStorageClient.delete(existingLinkPicture);

        verify(uploader).destroy(existingLinkPicture.getLargeImagePublicId(), cloudinaryDeleteOptions);
        verify(uploader).destroy(existingLinkPicture.getSmallImagePublicId(), cloudinaryDeleteOptions);
    }

    @Test
    void shouldThrowImageStorageExceptionWhenDeletionFailsForLargeImage() throws IOException {
        LinkPicture existingLinkPicture = LinkPictureFactory.random().build();
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(existingLinkPicture.getLargeImagePublicId(), cloudinaryDeleteOptions))
                .thenThrow(IOException.class);

        assertThrows(CloudinaryImageStorageException.class, () -> {
            cloudinaryMediaStorageClient.delete(existingLinkPicture);
        });
    }

    @Test
    void shouldThrowImageStorageExceptionWhenDeletionFailsForSmallImage() throws IOException {
        LinkPicture existingLinkPicture = LinkPictureFactory.random().build();
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(existingLinkPicture.getLargeImagePublicId(), cloudinaryDeleteOptions))
                .thenReturn(Map.of());
        when(uploader.destroy(existingLinkPicture.getSmallImagePublicId(), cloudinaryDeleteOptions))
                .thenThrow(IOException.class);

        assertThrows(CloudinaryImageStorageException.class, () -> {
            cloudinaryMediaStorageClient.delete(existingLinkPicture);
        });
    }
}