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

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import ec.animal.adoption.clients.gcloud.factories.GoogleCloudStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

import static com.google.cloud.storage.Acl.*;

@Component
public class GoogleCloudStorageClient {

    @Value("${google.cloud.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    @Autowired
    public GoogleCloudStorageClient(GoogleCloudStorageFactory googleCloudStorageFactory) {
        this.storage = googleCloudStorageFactory.get();
    }

    public String storeMedia(String mediaPath, byte[] content) {
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, mediaPath).
                setAcl(new ArrayList<>(Collections.singletonList(of(User.ofAllUsers(), Role.READER)))).
                build();
        Blob blob = storage.create(blobInfo, content);

        return blob.getMediaLink();
    }

    public boolean deleteMedia(String mediaPath) {
        BlobId blobId = BlobId.of(bucketName, mediaPath);
         return storage.delete(blobId);
    }
}
