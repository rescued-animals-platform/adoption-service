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

package ec.animal.adoption.client.gcloud;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import ec.animal.adoption.builders.ImagePictureBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.client.MediaStorageClient;
import ec.animal.adoption.client.gcloud.factories.GoogleCloudStorageFactory;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exception.ImageStorageException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(MockitoJUnitRunner.class)
public class MediaStorageClientGoogleCloudStorageTest {

    @Mock
    private Storage storage;

    private MediaStorageClientGoogleCloudStorage mediaStorageClientGoogleCloudStorage;

    @Before
    public void setUp() {
        GoogleCloudStorageFactory googleCloudStorageFactory = mock(GoogleCloudStorageFactory.class);
        when(googleCloudStorageFactory.get()).thenReturn(storage);
        mediaStorageClientGoogleCloudStorage = new MediaStorageClientGoogleCloudStorage(googleCloudStorageFactory);
        ReflectionTestUtils.setField(mediaStorageClientGoogleCloudStorage, "bucketName", randomAlphabetic(10));
    }

    @Test
    public void shouldBeAnInstanceOgMediaStorageClient() {
        assertThat(mediaStorageClientGoogleCloudStorage, is(instanceOf(MediaStorageClient.class)));
    }

    @Test
    public void shouldStorePicture() {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();

        String largeImageUrl = randomAlphabetic(10);
        Blob largeImageBlob = mock(Blob.class);
        when(largeImageBlob.getMediaLink()).thenReturn(largeImageUrl);
        when(storage.create(any(BlobInfo.class), eq(imagePicture.getLargeImageContent())))
                .thenReturn(largeImageBlob);

        String smallImageUrl = randomAlphabetic(10);
        Blob smallImageBlob = mock(Blob.class);
        when(smallImageBlob.getMediaLink()).thenReturn(smallImageUrl);
        when(storage.create(any(BlobInfo.class), eq(imagePicture.getSmallImageContent())))
                .thenReturn(smallImageBlob);

        LinkPicture expectedPicture = LinkPictureBuilder.random().withName(imagePicture.getName())
                .withPictureType(imagePicture.getPictureType()).withLargeImageMediaLink(new MediaLink(largeImageUrl))
                .withSmallImageMediaLink(new MediaLink(smallImageUrl)).build();

        LinkPicture storedPicture = mediaStorageClientGoogleCloudStorage.save(imagePicture);

        assertReflectionEquals(expectedPicture, storedPicture);
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringLargeImage() {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        when(storage.create(any(BlobInfo.class), eq(imagePicture.getLargeImageContent())))
                .thenThrow(StorageException.class);

        mediaStorageClientGoogleCloudStorage.save(imagePicture);
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringSmallImage() {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        when(storage.create(any(BlobInfo.class), eq(imagePicture.getLargeImageContent())))
                .thenReturn(mock(Blob.class));
        when(storage.create(any(BlobInfo.class), eq(imagePicture.getSmallImageContent())))
                .thenThrow(StorageException.class);

        mediaStorageClientGoogleCloudStorage.save(imagePicture);
    }
}