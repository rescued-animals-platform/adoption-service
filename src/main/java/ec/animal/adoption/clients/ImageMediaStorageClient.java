package ec.animal.adoption.clients;

import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.Link;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;

public interface ImageMediaStorageClient {
    Link save(ImageMedia media) throws ImageMediaProcessingException;
}
