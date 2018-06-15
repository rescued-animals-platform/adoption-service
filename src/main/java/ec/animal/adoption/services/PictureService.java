package ec.animal.adoption.services;

import ec.animal.adoption.clients.MediaStorageClient;
import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.repositories.PictureRepository;

public class PictureService {

    private final MediaStorageClient mediaStorageClient;
    private final PictureRepository pictureRepository;

    public PictureService(MediaStorageClient mediaStorageClient, PictureRepository pictureRepository) {
        this.mediaStorageClient = mediaStorageClient;
        this.pictureRepository = pictureRepository;
    }

    public Picture create(Picture picture) {
        Picture savedPicture = mediaStorageClient.save(picture);
        return pictureRepository.save(savedPicture);
    }
}
