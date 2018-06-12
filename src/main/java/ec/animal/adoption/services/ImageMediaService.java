package ec.animal.adoption.services;

import ec.animal.adoption.clients.ImageMediaStorageClient;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.Link;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;
import ec.animal.adoption.repositories.MediaLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageMediaService {

    private final ImageMediaStorageClient imageMediaStorageClient;
    private final MediaLinkRepository mediaLinkRepository;

    @Autowired
    public ImageMediaService(ImageMediaStorageClient imageMediaStorageClient, MediaLinkRepository mediaLinkRepository) {
        this.mediaLinkRepository = mediaLinkRepository;
        this.imageMediaStorageClient = imageMediaStorageClient;
    }

    public Link create(ImageMedia media) throws ImageMediaProcessingException, EntityAlreadyExistsException {
        Link link = imageMediaStorageClient.save(media);
        return mediaLinkRepository.save(link);
    }
}
