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

package ec.animal.adoption.adapter.rest.resource;

import ec.animal.adoption.adapter.rest.jwt.AdminTokenUtils;
import ec.animal.adoption.adapter.rest.model.media.LinkPictureResponse;
import ec.animal.adoption.adapter.rest.service.LinkPictureResponseMapper;
import ec.animal.adoption.application.OrganizationService;
import ec.animal.adoption.application.PictureService;
import ec.animal.adoption.domain.model.exception.InvalidPictureException;
import ec.animal.adoption.domain.model.media.Image;
import ec.animal.adoption.domain.model.media.ImagePicture;
import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.media.PictureType;
import ec.animal.adoption.domain.model.organization.Organization;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PictureResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureResource.class);

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
    public LinkPictureResponse createPrimaryPicture(@PathVariable("id") final UUID animalId,
                                                    @RequestParam("name") final String name,
                                                    @RequestParam("pictureType") final PictureType pictureType,
                                                    @RequestPart("largeImage") final MultipartFile largeImageMultipartFile,
                                                    @RequestPart("smallImage") final MultipartFile smallImageMultipartFile,
                                                    @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        ImagePicture imagePicture = new ImagePicture(name,
                                                     pictureType,
                                                     createImageFromMultipartFile(largeImageMultipartFile),
                                                     createImageFromMultipartFile(smallImageMultipartFile));
        LinkPicture linkPicture = pictureService.createFor(animalId, organization, imagePicture);

        return LinkPictureResponseMapper.MAPPER.toLinkPictureResponse(linkPicture);
    }

    @PutMapping(path = "/admin/animals/{id}/pictures",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public LinkPictureResponse updatePrimaryPicture(@PathVariable("id") final UUID animalId,
                                                    @RequestParam("name") final String name,
                                                    @RequestParam("pictureType") final PictureType pictureType,
                                                    @RequestPart("largeImage") final MultipartFile largeImageMultipartFile,
                                                    @RequestPart("smallImage") final MultipartFile smallImageMultipartFile,
                                                    @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        ImagePicture imagePicture = new ImagePicture(name,
                                                     pictureType,
                                                     createImageFromMultipartFile(largeImageMultipartFile),
                                                     createImageFromMultipartFile(smallImageMultipartFile));
        LinkPicture linkPicture = pictureService.updateFor(animalId, organization, imagePicture);

        return LinkPictureResponseMapper.MAPPER.toLinkPictureResponse(linkPicture);
    }

    private Image createImageFromMultipartFile(final MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();

        if (multipartFile.isEmpty() || originalFilename == null) {
            var originalFilenameLogSafe = Optional.ofNullable(originalFilename)
                                                  .map(s -> s.replaceAll("[\n\r\t]", "_"))
                                                  .orElse(null);
            LOGGER.info("Invalid picture: multipartFile.isEmpty()={}, originalFilename={}",
                        multipartFile.isEmpty(), originalFilenameLogSafe);
            throw new InvalidPictureException();
        }

        try {
            return new Image(FilenameUtils.getExtension(originalFilename),
                             multipartFile.getBytes(),
                             multipartFile.getSize());
        } catch (IOException exception) {
            throw new InvalidPictureException(exception);
        }
    }

    @GetMapping("/animals/{id}/pictures")
    public LinkPictureResponse get(@PathVariable("id") final UUID animalId) {
        LinkPicture linkPicture = pictureService.getBy(animalId);

        return LinkPictureResponseMapper.MAPPER.toLinkPictureResponse(linkPicture);
    }
}
