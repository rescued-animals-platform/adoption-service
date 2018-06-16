package ec.animal.adoption.clients;

import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.exceptions.ImageProcessingException;

public interface MediaStorageClient {
    MediaLink save(ImageMedia media) throws ImageProcessingException;

    Picture save(Picture picture);
}
