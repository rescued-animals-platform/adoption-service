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
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import ec.animal.adoption.domain.exception.MediaStorageException;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.MediaRepository;
import ec.animal.adoption.repository.exception.CloudinaryImageStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class MediaRepositoryCloudinary implements MediaRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(MediaRepositoryCloudinary.class);

    private final Cloudinary cloudinary;

    @Autowired
    public MediaRepositoryCloudinary(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    @HystrixCommand(fallbackMethod = "saveFallback")
    public LinkPicture save(final ImagePicture imagePicture) {
        try {
            MediaLink largeMediaLink = storeMedia(imagePicture.getLargeImageContent());
            MediaLink smallMediaLink = storeMedia(imagePicture.getSmallImageContent());

            return new LinkPicture(
                    imagePicture.getName(),
                    imagePicture.getPictureType(),
                    largeMediaLink,
                    smallMediaLink
            );
        } catch (IOException exception) {
            LOGGER.error("Exception thrown when communicating to Cloudinary", exception);
            throw new CloudinaryImageStorageException(exception);
        }
    }

    private MediaLink storeMedia(final byte[] content) throws IOException {
        Map<String, String> options = Map.of("folder", "default-organization");
        String url = cloudinary.uploader().upload(content, options).get("url").toString();
        return new MediaLink(url);
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod", "PMD.UnusedFormalParameter"})
    private LinkPicture saveFallback(final ImagePicture imagePicture) {
        LOGGER.info("Fallback for Cloudinary");
        throw new MediaStorageException();
    }
}
