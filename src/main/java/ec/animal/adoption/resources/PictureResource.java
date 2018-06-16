package ec.animal.adoption.resources;

import com.google.common.io.Files;
import ec.animal.adoption.domain.media.*;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/animals/{animalUuid}/pictures")
public class PictureResource {

    private final PictureService pictureService;

    @Autowired
    public PictureResource(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Picture create(
            @PathVariable("animalUuid") UUID animalUuid,
            @RequestParam("name") String name,
            @RequestParam("pictureType") PictureType pictureType,
            @RequestPart("largeImage") MultipartFile largeImageMultipartFile,
            @RequestPart("smallImage") MultipartFile smallImageMultipartFile
    ) {
        return pictureService.create(
                new ImagePicture(
                        animalUuid,
                        name,
                        pictureType,
                        createImageFromMultipartFile(largeImageMultipartFile),
                        createImageFromMultipartFile(smallImageMultipartFile)
                )
        );
    }

    private Image createImageFromMultipartFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty() || multipartFile.getOriginalFilename() == null) {
            throw new ImageProcessingException();
        }

        try {
            return new Image(
                    Files.getFileExtension(multipartFile.getOriginalFilename()),
                    multipartFile.getBytes(),
                    multipartFile.getSize()
            );
        } catch (IOException e) {
            throw new ImageProcessingException();
        }
    }
}
