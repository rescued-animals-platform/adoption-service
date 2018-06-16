package ec.animal.adoption.services;

import ec.animal.adoption.clients.MediaStorageClient;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.repositories.MediaLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageMediaService {

    private final MediaStorageClient mediaStorageClient;
    private final MediaLinkRepository mediaLinkRepository;

    @Autowired
    public ImageMediaService(MediaStorageClient mediaStorageClient, MediaLinkRepository mediaLinkRepository) {
        this.mediaLinkRepository = mediaLinkRepository;
        this.mediaStorageClient = mediaStorageClient;
    }

    public MediaLink create(ImageMedia media) {
        MediaLink mediaLink = mediaStorageClient.save(media);
        return mediaLinkRepository.save(mediaLink);
    }
}
