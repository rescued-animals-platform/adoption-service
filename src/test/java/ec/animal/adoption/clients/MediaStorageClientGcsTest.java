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

package ec.animal.adoption.clients;

import com.google.cloud.storage.StorageException;
import ec.animal.adoption.builders.ImagePictureBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.clients.gcloud.GoogleCloudStorageClient;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.ImageStorageException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MediaStorageClientGcsTest {

    @Mock
    private GoogleCloudStorageClient googleCloudStorageClient;

    private MediaStorageClientGcs mediaStorageClientGcs;

    @Before
    public void setUp() {
        mediaStorageClientGcs = new MediaStorageClientGcs(googleCloudStorageClient);
    }

    @Test
    public void shouldBeAnInstanceOgMediaStorageClient() {
        assertThat(mediaStorageClientGcs, is(instanceOf(MediaStorageClient.class)));
    }

    @Test
    public void shouldStorePicture() {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        String largeImageUrl = randomAlphabetic(10);
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getLargeImagePath(), imagePicture.getLargeImageContent()
        )).thenReturn(largeImageUrl);
        String smallImageUrl = randomAlphabetic(10);
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getSmallImagePath(), imagePicture.getSmallImageContent()
        )).thenReturn(smallImageUrl);
        LinkPicture expectedPicture = LinkPictureBuilder.random().withAnimalUuid(imagePicture.getAnimalUuid()).
                withName(imagePicture.getName()).withPictureType(imagePicture.getPictureType()).
                withLargeImageMediaLink(new MediaLink(largeImageUrl)).
                withSmallImageMediaLink(new MediaLink(smallImageUrl)).build();

        LinkPicture storedPicture = mediaStorageClientGcs.save(imagePicture);

        assertThat(storedPicture, is(expectedPicture));
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringLargeImage() {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getLargeImagePath(), imagePicture.getLargeImageContent()
        )).thenThrow(StorageException.class);

        mediaStorageClientGcs.save(imagePicture);
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringSmallImage() {
        ImagePicture imagePicture = ImagePictureBuilder.random().build();
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getSmallImagePath(), imagePicture.getSmallImageContent()
        )).thenThrow(StorageException.class);

        mediaStorageClientGcs.save(imagePicture);
    }
}