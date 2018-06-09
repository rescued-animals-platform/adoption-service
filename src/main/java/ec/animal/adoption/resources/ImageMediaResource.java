package ec.animal.adoption.resources;

import com.google.common.io.Files;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;
import ec.animal.adoption.services.ImageMediaService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class ImageMediaResource {

    private final ImageMediaService imageMediaService;

    public ImageMediaResource(ImageMediaService imageMediaService) {
        this.imageMediaService = imageMediaService;
    }

    public MediaLink create(UUID animalUuid, MultipartFile multipartFile) throws ImageMediaProcessingException {
        return imageMediaService.create(createImageMediaFromMultipartFile(animalUuid, multipartFile));
    }

    private ImageMedia createImageMediaFromMultipartFile(UUID animalUuid, MultipartFile multipartFile) throws
            ImageMediaProcessingException {
        if (multipartFile.isEmpty() || multipartFile.getOriginalFilename() == null) {
            throw new ImageMediaProcessingException();
        }

        try {
            return new ImageMedia(
                    animalUuid,
                    Files.getFileExtension(multipartFile.getOriginalFilename()),
                    multipartFile.getBytes(),
                    multipartFile.getSize()
            );
        } catch (IOException e) {
            throw new ImageMediaProcessingException();
        }
    }
}
