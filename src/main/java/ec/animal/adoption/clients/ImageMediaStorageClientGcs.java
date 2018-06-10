package ec.animal.adoption.clients;

import com.google.cloud.storage.StorageException;
import ec.animal.adoption.clients.gcloud.GoogleCloudStorageClient;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageMediaStorageClientGcs implements ImageMediaStorageClient {

    private final GoogleCloudStorageClient googleCloudStorageClient;

    @Autowired
    public ImageMediaStorageClientGcs(GoogleCloudStorageClient googleCloudStorageClient) {
        this.googleCloudStorageClient = googleCloudStorageClient;
    }

    @Override
    public MediaLink save(ImageMedia imageMedia) throws ImageMediaProcessingException {
        try {
            String url = googleCloudStorageClient.storeMedia(imageMedia.getPath(), imageMedia.getContent());
            return new MediaLink(imageMedia.getAnimalUuid(), imageMedia.getName(), url);
        } catch (StorageException ex) {
            throw new ImageMediaProcessingException();
        }
    }
}
