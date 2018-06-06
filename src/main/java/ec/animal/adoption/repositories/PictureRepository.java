package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.picture.Picture;

public interface PictureRepository {
    Picture save(Picture picture);
}
