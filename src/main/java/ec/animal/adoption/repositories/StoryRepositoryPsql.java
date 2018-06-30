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

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaStory;
import ec.animal.adoption.repositories.jpa.JpaStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class StoryRepositoryPsql implements StoryRepository {

    private final JpaStoryRepository jpaStoryRepository;
    private final AnimalRepositoryPsql animalRepositoryPsql;

    @Autowired
    public StoryRepositoryPsql(JpaStoryRepository jpaStoryRepository, AnimalRepositoryPsql animalRepositoryPsql) {
        this.jpaStoryRepository = jpaStoryRepository;
        this.animalRepositoryPsql = animalRepositoryPsql;
    }

    @Override
    public Story save(Story story) {
        if(!animalRepositoryPsql.animalExists(story.getAnimalUuid())) {
            throw new EntityNotFoundException();
        }

        try {
            JpaStory jpaStory = jpaStoryRepository.save(new JpaStory(story));
            return jpaStory.toStory();
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }

    @Override
    public Story getBy(UUID animalUuid) {
        Optional<JpaStory> jpaStory = jpaStoryRepository.findByAnimalUuid(animalUuid);

        if(!jpaStory.isPresent()) {
            throw new EntityNotFoundException();
        }

        return jpaStory.get().toStory();
    }
}
