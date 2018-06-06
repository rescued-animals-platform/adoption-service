package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Picture;
import ec.animal.adoption.services.PictureService;

public class PictureResource {

    private final PictureService pictureService;

    public PictureResource(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    public Picture create(Picture picture) {
        return pictureService.create(picture);
    }
}
