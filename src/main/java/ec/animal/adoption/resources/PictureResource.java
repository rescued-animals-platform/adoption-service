package ec.animal.adoption.resources;

import ec.animal.adoption.domain.picture.Image;
import ec.animal.adoption.domain.picture.Picture;
import ec.animal.adoption.exceptions.ImageProcessingException;
import ec.animal.adoption.services.PictureService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class PictureResource {

    private final PictureService pictureService;

    public PictureResource(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    public Picture create(MultipartFile multipartFile) throws ImageProcessingException {
        return pictureService.create(new Picture(multipartFile.getName(), new Image(getInputStream(multipartFile))));
    }

    private InputStream getInputStream(MultipartFile multipartFile) throws ImageProcessingException {
        try {
            return multipartFile.getInputStream();
        } catch (IOException e) {
            throw new ImageProcessingException();
        }
    }
}
