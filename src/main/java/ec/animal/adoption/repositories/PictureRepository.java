package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.Picture;

public interface PictureRepository {
    Picture save(Picture picture);
}
