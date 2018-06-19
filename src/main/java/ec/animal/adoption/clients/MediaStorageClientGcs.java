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
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.exceptions.ImageStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaStorageClientGcs implements MediaStorageClient {

    private final GoogleCloudStorageClient googleCloudStorageClient;

    @Autowired
    public MediaStorageClientGcs(GoogleCloudStorageClient googleCloudStorageClient) {
        this.googleCloudStorageClient = googleCloudStorageClient;
    }

    @Override
    public Picture save(Picture picture) {
        try {
            return getPicture(picture);
        } catch (StorageException ex) {
            throw new ImageStorageException();
        }
    }

    private Picture getPicture(Picture picture) {
        if (picture.hasImages()) {
            String largeImageUrl = googleCloudStorageClient.storeMedia(
                    picture.getLargeImagePath(), picture.getLargeImageContent()
            );
            String smallImageUrl = googleCloudStorageClient.storeMedia(
                    picture.getSmallImagePath(), picture.getSmallImageContent()
            );
            return new LinkPicture(
                    picture.getAnimalUuid(),
                    picture.getName(),
                    picture.getPictureType(),
                    new MediaLink(largeImageUrl),
                    new MediaLink(smallImageUrl)
            );
        }

        throw new IllegalArgumentException();
    }
}
