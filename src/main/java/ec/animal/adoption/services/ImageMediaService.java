package ec.animal.adoption.services;

import ec.animal.adoption.clients.MediaStorageClient;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;
import ec.animal.adoption.repositories.PictureRepository;

public class ImageMediaService {

    private final MediaStorageClient mediaStorageClient;
    private final PictureRepository pictureRepository;

    public ImageMediaService(MediaStorageClient mediaStorageClient, PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
        this.mediaStorageClient = mediaStorageClient;
    }

    public MediaLink create(ImageMedia media) throws ImageMediaProcessingException {
        MediaLink mediaLink = mediaStorageClient.save(media);
        return pictureRepository.save(mediaLink);
    }
}
