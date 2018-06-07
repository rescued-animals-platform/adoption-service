package ec.animal.adoption.resources;

import ec.animal.adoption.domain.picture.Image;
import ec.animal.adoption.domain.picture.Picture;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.services.PictureService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PictureResource {

    private final PictureService pictureService;

    public PictureResource(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    public Picture create(MultipartFile multipartFile) throws ImageProcessingException {
        return pictureService.create(createPictureFromMultipartFile(multipartFile));
    }

    private Picture createPictureFromMultipartFile(MultipartFile multipartFile) throws ImageProcessingException {
        if(multipartFile.isEmpty()) {
            throw new ImageProcessingException();
        }

        try {
            return new Picture(
                    multipartFile.getName(),
                    multipartFile.getOriginalFilename(),
                    multipartFile.getSize(),
                    new Image(multipartFile.getInputStream(), multipartFile.getBytes())
            );
        } catch (IOException e) {
            throw new ImageProcessingException();
        }
    }
}
