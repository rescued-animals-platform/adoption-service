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

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import ec.animal.adoption.clients.factories.GoogleCloudStorageFactory;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.ImageStorageException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

import static com.google.cloud.storage.Acl.of;

@Component
public class MediaStorageClientGoogleCloudStorage implements MediaStorageClient {

    @Value("${google.cloud.storage.bucket}")
    private String bucketName;

    @Value("${google.cloud.storage.environment}")
    private String environment;

    private final GoogleCloudStorageFactory googleCloudStorageFactory;
    private static final Log LOG = LogFactory.getLog(MediaStorageClientGoogleCloudStorage.class);

    @Autowired
    public MediaStorageClientGoogleCloudStorage(final GoogleCloudStorageFactory googleCloudStorageFactory) {
        this.googleCloudStorageFactory = googleCloudStorageFactory;
    }

    @Override
    public LinkPicture save(final ImagePicture imagePicture) {
        try {
            return storeImagePicture(imagePicture);
        } catch (StorageException ex) {
            LOG.error("Image Storage Exception will be thrown", ex);
            throw new ImageStorageException(ex);
        }
    }

    private LinkPicture storeImagePicture(final ImagePicture imagePicture) {
        final String largeImageUrl = storeMediaAndGetLink(
                imagePicture.getLargeImagePath(),
                imagePicture.getLargeImageContent()
        );
        final String smallImageUrl = storeMediaAndGetLink(
                imagePicture.getSmallImagePath(),
                imagePicture.getSmallImageContent()
        );

        return new LinkPicture(
                imagePicture.getAnimalUuid(),
                imagePicture.getName(),
                imagePicture.getPictureType(),
                new MediaLink(largeImageUrl),
                new MediaLink(smallImageUrl)
        );
    }

    private String storeMediaAndGetLink(final String mediaPath, final byte[] content) {
        final Storage storage = googleCloudStorageFactory.get(environment);
        final BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, mediaPath).
                setAcl(new ArrayList<>(Collections.singletonList(of(Acl.User.ofAllUsers(), Acl.Role.READER)))).
                build();

        return storage.create(blobInfo, content).getMediaLink();
    }
}
