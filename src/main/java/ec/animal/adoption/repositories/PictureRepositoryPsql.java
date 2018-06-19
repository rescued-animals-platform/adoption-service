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

package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaLinkPicture;
import ec.animal.adoption.repositories.jpa.JpaLinkPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PictureRepositoryPsql implements PictureRepository {

    private final JpaLinkPictureRepository jpaLinkPictureRepository;
    private final AnimalRepositoryPsql animalRepositoryPsql;

    @Autowired
    public PictureRepositoryPsql(
            JpaLinkPictureRepository jpaLinkPictureRepository, AnimalRepositoryPsql animalRepositoryPsql
    ) {
        this.jpaLinkPictureRepository = jpaLinkPictureRepository;
        this.animalRepositoryPsql = animalRepositoryPsql;
    }

    @Override
    public Picture save(Picture picture) {
        UUID animalUuid = picture.getAnimalUuid();

        if(!animalRepositoryPsql.animalExists(animalUuid)) {
            throw new EntityNotFoundException();
        }

        if(primaryLinkPictureExists(animalUuid)) {
            throw new EntityAlreadyExistsException();
        }

        return saveJpaLinkPicture(new JpaLinkPicture(picture)).toPicture();
    }

    private boolean primaryLinkPictureExists(UUID animalUuid) {
        Optional<JpaLinkPicture> jpaPrimaryLinkPicture = jpaLinkPictureRepository.findByPictureTypeAndAnimalUuid(
                PictureType.PRIMARY.name(), animalUuid
        );
        return jpaPrimaryLinkPicture.isPresent();
    }

    private JpaLinkPicture saveJpaLinkPicture(JpaLinkPicture jpaLinkPicture) {
        try {
            return jpaLinkPictureRepository.save(jpaLinkPicture);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
