package ec.animal.adoption.clients;

import ec.animal.adoption.domain.media.Picture;

public interface MediaStorageClient {

    Picture save(Picture picture);
}
