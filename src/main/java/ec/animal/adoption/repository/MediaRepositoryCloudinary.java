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

package ec.animal.adoption.repository;

import com.cloudinary.Cloudinary;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.MediaRepository;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.repository.exception.CloudinaryImageStorageException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class MediaRepositoryCloudinary implements MediaRepository {

    private static final String MEDIA_REPOSITORY = "media-repository";
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaRepositoryCloudinary.class);

    private final Cloudinary cloudinary;

    @Autowired
    public MediaRepositoryCloudinary(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    @CircuitBreaker(name = MEDIA_REPOSITORY)
    public LinkPicture save(@NonNull final ImagePicture imagePicture, @NonNull final Organization organization) {
        try {
            UUID organizationId = organization.getOrganizationId();
            MediaLink largeMediaLink = storeMedia(imagePicture.getLargeImageContent(), organizationId);
            MediaLink smallMediaLink = storeMedia(imagePicture.getSmallImageContent(), organizationId);

            return new LinkPicture(imagePicture.getName(),
                                   imagePicture.getPictureType(),
                                   largeMediaLink,
                                   smallMediaLink);
        } catch (IOException exception) {
            LOGGER.error("Exception thrown when uploading resource in Cloudinary");
            throw new CloudinaryImageStorageException(exception);
        }
    }

    private MediaLink storeMedia(final byte[] content, final UUID organizationId) throws IOException {
        Map<String, String> options = Map.of("folder", organizationId.toString());
        Map<?, ?> uploadResponse = cloudinary.uploader().upload(content, options);
        String publicId = uploadResponse.get("public_id").toString();
        String url = uploadResponse.get("url").toString();
        return new MediaLink(publicId, url);
    }

    @Override
    @CircuitBreaker(name = MEDIA_REPOSITORY, fallbackMethod = "deleteFallback")
    public void delete(@NonNull final LinkPicture existingLinkPicture) {
        try {
            Map<String, String> options = Map.of("invalidate", "true");
            cloudinary.uploader().destroy(existingLinkPicture.getLargeImagePublicId(), options);
            cloudinary.uploader().destroy(existingLinkPicture.getSmallImagePublicId(), options);
        } catch (IOException exception) {
            LOGGER.error("Exception thrown when deleting resource in Cloudinary");
            throw new CloudinaryImageStorageException(exception);
        }
    }

    @SuppressWarnings(value = "java:S1144")
    private void deleteFallback(@NonNull final LinkPicture existingLinkPicture, Exception ex) {
        LOGGER.warn("Fallback called with Exception", ex);
        LOGGER.info("Fallback for Cloudinary delete. Couldn't delete one or more resources with public ids: {}, {}",
                    existingLinkPicture.getLargeImagePublicId(),
                    existingLinkPicture.getSmallImagePublicId());
    }
}
