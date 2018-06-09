package ec.animal.adoption.clients;

import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;

public interface MediaStorageClient {
    MediaLink save(ImageMedia media) throws ImageMediaProcessingException;
}
