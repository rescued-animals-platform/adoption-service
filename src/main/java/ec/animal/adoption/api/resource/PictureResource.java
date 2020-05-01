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

package ec.animal.adoption.api.resource;

import ec.animal.adoption.api.jwt.AdminTokenUtils;
import ec.animal.adoption.domain.exception.InvalidPictureException;
import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureService;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class PictureResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(PictureResource.class);

    private final PictureService pictureService;
    private final OrganizationService organizationService;
    private final AdminTokenUtils adminTokenUtils;

    @Autowired
    public PictureResource(final PictureService pictureService,
                           final OrganizationService organizationService,
                           final AdminTokenUtils adminTokenUtils) {
        this.pictureService = pictureService;
        this.organizationService = organizationService;
        this.adminTokenUtils = adminTokenUtils;
    }

    @PostMapping(path = "/admin/animals/{id}/pictures",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public LinkPicture createPrimaryPicture(
            @PathVariable("id") final UUID animalId,
            @RequestParam("name") final String name,
            @RequestParam("pictureType") final PictureType pictureType,
            @RequestPart("largeImage") final MultipartFile largeImageMultipartFile,
            @RequestPart("smallImage") final MultipartFile smallImageMultipartFile,
            @AuthenticationPrincipal final Jwt token
    ) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        return pictureService.createFor(
                animalId,
                organization,
                new ImagePicture(
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
            LOGGER.info("Invalid picture: multipartFile.isEmpty()={}, originalFilename={}",
                        multipartFile.isEmpty(), originalFilename);
            throw new InvalidPictureException();
        }

        try {
            return new Image(FilenameUtils.getExtension(originalFilename),
                             multipartFile.getBytes(),
                             multipartFile.getSize());
        } catch (IOException exception) {
            LOGGER.error("Exception thrown when creating an image for {}", originalFilename, exception);
            throw new InvalidPictureException(exception);
        }
    }

    @GetMapping("/animals/{id}/pictures")
    public LinkPicture get(@PathVariable("id") final UUID animalId) {
        return pictureService.getBy(animalId);
    }
}
