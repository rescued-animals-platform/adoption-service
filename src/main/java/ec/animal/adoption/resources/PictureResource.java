/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.resources;

import com.google.common.io.Files;
import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.exceptions.InvalidPictureException;
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
    public PictureResource(final PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public LinkPicture createPrimaryPicture(
            @PathVariable("animalUuid") final UUID animalUuid,
            @RequestParam("name") final String name,
            @RequestParam("pictureType") final PictureType pictureType,
            @RequestPart("largeImage") final MultipartFile largeImageMultipartFile,
            @RequestPart("smallImage") final MultipartFile smallImageMultipartFile
    ) {
        return pictureService.createPrimaryPicture(
                new ImagePicture(
                        animalUuid,
                        name,
                        pictureType,
                        createImageFromMultipartFile(largeImageMultipartFile),
                        createImageFromMultipartFile(smallImageMultipartFile)
                )
        );
    }

    private Image createImageFromMultipartFile(final MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();

        if (multipartFile.isEmpty() || originalFilename == null) {
            throw new InvalidPictureException();
        }

        try {
            return new Image(
                    Files.getFileExtension(originalFilename), multipartFile.getBytes(), multipartFile.getSize()
            );
        } catch (IOException exception) {
            throw new InvalidPictureException(exception);
        }
    }

    @GetMapping
    public LinkPicture get(@PathVariable("animalUuid") final UUID animalUuid) {
        return pictureService.getBy(animalUuid);
    }
}
