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
import ec.animal.adoption.clients.gcloud.GoogleCloudStorageClient;
import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.exceptions.ImageStorageException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MediaStorageClientGcsTest {

    @Mock
    private GoogleCloudStorageClient googleCloudStorageClient;

    private UUID animalUuid;
    private String name;
    private PictureType pictureType;
    private MediaStorageClientGcs mediaStorageClientGcs;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        name = randomAlphabetic(10);
        pictureType = getRandomPictureType();
        mediaStorageClientGcs = new MediaStorageClientGcs(googleCloudStorageClient);
    }

    @Test
    public void shouldBeAnInstanceOgMediaStorageClient() {
        assertThat(mediaStorageClientGcs, is(instanceOf(MediaStorageClient.class)));
    }

    @Test
    public void shouldStorePicture() {
        ImagePicture imagePicture = new ImagePicture(
                animalUuid, name, pictureType, mock(Image.class), mock(Image.class)
        );
        String largeImageUrl = randomAlphabetic(10);
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getLargeImagePath(), imagePicture.getLargeImageContent()
        )).thenReturn(largeImageUrl);
        String smallImageUrl = randomAlphabetic(10);
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getSmallImagePath(), imagePicture.getSmallImageContent()
        )).thenReturn(smallImageUrl);
        LinkPicture expectedPicture = new LinkPicture(
                animalUuid, name, pictureType, new MediaLink(largeImageUrl), new MediaLink(smallImageUrl)
        );

        LinkPicture storedPicture = mediaStorageClientGcs.save(imagePicture);

        assertThat(storedPicture, is(expectedPicture));
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringLargeImage() {
        ImagePicture imagePicture = new ImagePicture(
                animalUuid, name, pictureType, mock(Image.class), mock(Image.class)
        );
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getLargeImagePath(), imagePicture.getLargeImageContent()
        )).thenThrow(StorageException.class);

        mediaStorageClientGcs.save(imagePicture);
    }

    @Test(expected = ImageStorageException.class)
    public void shouldThrowImageStorageExceptionWhenStoringSmallImage() {
        ImagePicture imagePicture = new ImagePicture(
                animalUuid, name, pictureType, mock(Image.class), mock(Image.class)
        );
        when(googleCloudStorageClient.storeMedia(
                imagePicture.getSmallImagePath(), imagePicture.getSmallImageContent()
        )).thenThrow(StorageException.class);

        mediaStorageClientGcs.save(imagePicture);
    }
}