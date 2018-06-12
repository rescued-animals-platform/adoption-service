package ec.animal.adoption.resources;

import com.google.common.io.Files;
import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.ImageMediaProcessingException;
import ec.animal.adoption.services.ImageMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/animals/{animalUuid}/image")
public class ImageMediaResource {

    private final ImageMediaService imageMediaService;

    @Autowired
    public ImageMediaResource(ImageMediaService imageMediaService) {
        this.imageMediaService = imageMediaService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MediaLink create(
            @PathVariable("animalUuid") UUID animalUuid, @RequestPart("file") MultipartFile multipartFile
    ) throws ImageMediaProcessingException, EntityAlreadyExistsException {
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
