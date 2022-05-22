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

package ec.animal.adoption.domain.media;

import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalBuilder;
import ec.animal.adoption.domain.animal.AnimalRepository;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.exception.InvalidPictureException;
import ec.animal.adoption.domain.organization.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PictureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureService.class);

    private final MediaRepository mediaRepository;
    private final AnimalRepository animalRepository;

    @Autowired
    public PictureService(final MediaRepository mediaRepository,
                          final AnimalRepository animalRepository) {
        this.mediaRepository = mediaRepository;
        this.animalRepository = animalRepository;
    }

    public LinkPicture createFor(final UUID animalId,
                                 final Organization organization,
                                 final ImagePicture imagePicture) {

        if (!PictureType.PRIMARY.equals(imagePicture.getPictureType()) || !imagePicture.isValid()) {
            LOGGER.info("Invalid picture with type: {}", imagePicture.getPictureType());
            throw new InvalidPictureException();
        }

        Animal animal = animalRepository.getBy(animalId, organization);
        animal.getPrimaryLinkPicture().ifPresent(p -> {
            throw new EntityAlreadyExistsException();
        });

        LinkPicture linkPicture = mediaRepository.save(imagePicture, organization);
        Animal animalWithPrimaryLinkPicture = AnimalBuilder.copyOf(animal).with(linkPicture).build();

        return animalRepository.save(animalWithPrimaryLinkPicture).getPrimaryLinkPicture().orElseThrow();
    }

    public LinkPicture updateFor(final UUID animalId,
                                 final Organization organization,
                                 final ImagePicture imagePicture) {

        if (!PictureType.PRIMARY.equals(imagePicture.getPictureType()) || !imagePicture.isValid()) {
            LOGGER.info("Invalid picture with type: {}", imagePicture.getPictureType());
            throw new InvalidPictureException();
        }

        Animal animal = animalRepository.getBy(animalId, organization);
        LinkPicture linkPicture = mediaRepository.save(imagePicture, organization);
        animal.getPrimaryLinkPicture().ifPresent(mediaRepository::delete);
        Animal animalWithUpdatedPrimaryLinkPicture = AnimalBuilder.copyOf(animal).with(linkPicture).build();

        return animalRepository.save(animalWithUpdatedPrimaryLinkPicture).getPrimaryLinkPicture().orElseThrow();
    }

    public LinkPicture getBy(final UUID animalId) {
        Animal animal = animalRepository.getBy(animalId);
        return animal.getPrimaryLinkPicture().orElseThrow(EntityNotFoundException::new);
    }
}
