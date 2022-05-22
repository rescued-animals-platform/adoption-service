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

package ec.animal.adoption.domain.story;

import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalBuilder;
import ec.animal.adoption.domain.animal.AnimalRepository;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.organization.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoryService.class);

    private final AnimalRepository animalRepository;

    @Autowired
    public StoryService(final AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Story createFor(final UUID animalId, final Organization organization, final Story story) {
        Animal animal = animalRepository.getBy(animalId, organization);
        animal.getStory().ifPresent(s -> {
            LOGGER.info("A story already exists for animal {}", animalId);
            throw new EntityAlreadyExistsException();
        });

        Animal animalWithStory = AnimalBuilder.copyOf(animal).with(story).build();
        return animalRepository.save(animalWithStory).getStory().orElseThrow();
    }

    public Story updateFor(final UUID animalId, final Organization organization, final Story story) {
        Animal animal = animalRepository.getBy(animalId, organization);

        if (animal.has(story)) {
            LOGGER.info("Attempt to update animal {} with the same story it already has", animalId);
            return story;
        }

        Animal animalWithUpdatedStory = AnimalBuilder.copyOf(animal).with(story).build();
        return animalRepository.save(animalWithUpdatedStory).getStory().orElseThrow();
    }

    public Story getBy(final UUID animalId) {
        Animal animal = animalRepository.getBy(animalId);
        return animal.getStory().orElseThrow(EntityNotFoundException::new);
    }
}
