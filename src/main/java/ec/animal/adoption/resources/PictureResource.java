package ec.animal.adoption.resources;

import com.google.common.io.Files;
import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.services.PictureService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class PictureResource {

    private final PictureService pictureService;

    public PictureResource(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    public Picture create(
            UUID animalUuid,
            String name,
            PictureType pictureType,
            MultipartFile largeImageMultipartFile,
            MultipartFile smallImageMultipartFile
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
