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

package ec.animal.adoption.service;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.Story;
import ec.animal.adoption.exception.EntityNotFoundException;
import ec.animal.adoption.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StoryService {

    private final AnimalRepository animalRepository;

    @Autowired
    public StoryService(final AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Story create(final UUID animalUuid, final Story story) {
        Animal animal = animalRepository.getBy(animalUuid);
        animal.setStory(story);

        return animalRepository.save(animal).getStory();
    }

    public Story getBy(final UUID animalUuid) {
        Animal animal = animalRepository.getBy(animalUuid);

        if (animal.getStory() == null) {
            throw new EntityNotFoundException();
        }

        return animal.getStory();
    }
}
