package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.MediaLink;

public interface PictureRepository {
    MediaLink save(MediaLink media);
}
