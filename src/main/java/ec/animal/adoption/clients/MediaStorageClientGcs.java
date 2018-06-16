package ec.animal.adoption.clients;

import com.google.cloud.storage.StorageException;
import ec.animal.adoption.clients.gcloud.GoogleCloudStorageClient;
import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.exceptions.ImageProcessingException;
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
    public MediaLink save(ImageMedia imageMedia) throws ImageProcessingException {
        try {
            String url = googleCloudStorageClient.storeMedia(imageMedia.getPath(), imageMedia.getContent());
            return new MediaLink(imageMedia.getAnimalUuid(), imageMedia.getName(), url);
        } catch (StorageException ex) {
            throw new ImageProcessingException();
        }
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
        if(picture.hasImages()) {
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
