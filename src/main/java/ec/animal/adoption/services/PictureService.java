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

package ec.animal.adoption.services;

import ec.animal.adoption.clients.MediaStorageClient;
import ec.animal.adoption.domain.media.ImagePicture;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.exceptions.InvalidPictureException;
import ec.animal.adoption.repositories.LinkPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PictureService {

    private final MediaStorageClient mediaStorageClient;
    private final LinkPictureRepository linkPictureRepository;

    @Autowired
    public PictureService(MediaStorageClient mediaStorageClient, LinkPictureRepository linkPictureRepository) {
        this.mediaStorageClient = mediaStorageClient;
        this.linkPictureRepository = linkPictureRepository;
    }

    public LinkPicture create(ImagePicture imagePicture) {
        if(!imagePicture.isValid()) {
            throw new InvalidPictureException();
        }

        LinkPicture linkPicture = mediaStorageClient.save(imagePicture);
        return linkPictureRepository.save(linkPicture);
    }

    public LinkPicture getBy(UUID animalUuid) {
        return linkPictureRepository.getBy(animalUuid);
    }
}
