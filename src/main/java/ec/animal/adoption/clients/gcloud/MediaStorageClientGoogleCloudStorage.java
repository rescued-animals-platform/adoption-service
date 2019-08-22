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

package ec.animal.adoption.clients.gcloud;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import ec.animal.adoption.clients.MediaStorageClient;
import ec.animal.adoption.clients.gcloud.factories.GoogleCloudStorageFactory;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.GoogleCloudStorageException;
import ec.animal.adoption.exceptions.ImageStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MediaStorageClientGoogleCloudStorage implements MediaStorageClient {

    @Value("${google.cloud.storage.bucket}")
    private String bucketName;

    private final GoogleCloudStorageFactory googleCloudStorageFactory;

    @Autowired
    public MediaStorageClientGoogleCloudStorage(final GoogleCloudStorageFactory googleCloudStorageFactory) {
        this.googleCloudStorageFactory = googleCloudStorageFactory;
    }

    @Override
    @HystrixCommand(defaultFallback = "circuitBreakerFallback", ignoreExceptions = {GoogleCloudStorageException.class})
    public LinkPicture save(final ImagePicture imagePicture) {
        try {
            return storeImagePicture(imagePicture);
        } catch (StorageException exception) {
            throw new ImageStorageException(exception);
        }
    }

    private LinkPicture storeImagePicture(final ImagePicture imagePicture) {
        String largeImageUrl = storeMediaAndGetLink(
                imagePicture.getLargeImagePath(),
                imagePicture.getLargeImageContent()
        );
        String smallImageUrl = storeMediaAndGetLink(
                imagePicture.getSmallImagePath(),
                imagePicture.getSmallImageContent()
        );

        return new LinkPicture(
                imagePicture.getName(),
                imagePicture.getPictureType(),
                new MediaLink(largeImageUrl),
                new MediaLink(smallImageUrl)
        );
    }

    private String storeMediaAndGetLink(final String mediaPath, final byte[] content) {
        Storage storage = googleCloudStorageFactory.get();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, mediaPath).build();

        return storage.create(blobInfo, content).getMediaLink();
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private LinkPicture circuitBreakerFallback() {
        throw new GoogleCloudStorageException();
    }
}
