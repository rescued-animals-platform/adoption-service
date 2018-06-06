package ec.animal.adoption.services;

import ec.animal.adoption.domain.picture.Picture;
import ec.animal.adoption.repositories.PictureRepository;

public class PictureService {

    private final PictureRepository pictureRepository;

    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public Picture create(Picture picture) {
        return pictureRepository.save(picture);
    }
}
