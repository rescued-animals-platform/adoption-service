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

import ec.animal.adoption.domain.media.LinkPicture;
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
public class LinkPictureRepositoryPsql implements LinkPictureRepository {

    private final JpaLinkPictureRepository jpaLinkPictureRepository;
    private final AnimalRepositoryPsql animalRepositoryPsql;

    @Autowired
    public LinkPictureRepositoryPsql(
            JpaLinkPictureRepository jpaLinkPictureRepository, AnimalRepositoryPsql animalRepositoryPsql
    ) {
        this.jpaLinkPictureRepository = jpaLinkPictureRepository;
        this.animalRepositoryPsql = animalRepositoryPsql;
    }

    @Override
    public LinkPicture save(LinkPicture linkPicture) {
        UUID animalUuid = linkPicture.getAnimalUuid();

        if(!animalRepositoryPsql.animalExists(animalUuid)) {
            throw new EntityNotFoundException();
        }

        if(getPrimaryLinkPicture(animalUuid).isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        return saveJpaLinkPicture(new JpaLinkPicture(linkPicture)).toLinkPicture();
    }

    @Override
    public LinkPicture getBy(UUID animalUuid) {
        Optional<JpaLinkPicture> primaryLinkPicture = getPrimaryLinkPicture(animalUuid);

        if(primaryLinkPicture.isPresent()) {
            return primaryLinkPicture.get().toLinkPicture();
        }

        throw new EntityNotFoundException();
    }

    private Optional<JpaLinkPicture> getPrimaryLinkPicture(UUID animalUuid) {
        return jpaLinkPictureRepository.findByPictureTypeAndAnimalUuid(PictureType.PRIMARY.name(), animalUuid);
    }

    private JpaLinkPicture saveJpaLinkPicture(JpaLinkPicture jpaLinkPicture) {
        try {
            return jpaLinkPictureRepository.save(jpaLinkPicture);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
