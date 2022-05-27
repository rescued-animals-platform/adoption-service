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

package ec.animal.adoption.application;

import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.AnimalBuilder;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.model.exception.EntityNotFoundException;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.service.AnimalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CharacteristicsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacteristicsService.class);

    private final AnimalRepository animalRepository;

    @Autowired
    public CharacteristicsService(final AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Characteristics createFor(final UUID animalId,
                                     final Organization organization,
                                     final Characteristics characteristics) {
        Animal animal = animalRepository.getBy(animalId, organization);
        animal.getCharacteristics().ifPresent(c -> {
            throw new EntityAlreadyExistsException();
        });

        Animal animalWithCharacteristics = AnimalBuilder.copyOf(animal).with(characteristics).build();
        return animalRepository.save(animalWithCharacteristics).getCharacteristics().orElseThrow();
    }

    public Characteristics updateFor(final UUID animalId,
                                     final Organization organization,
                                     final Characteristics characteristics) {
        Animal animal = animalRepository.getBy(animalId, organization);

        if (animal.has(characteristics)) {
            LOGGER.info("Attempt to update animal {} with the same characteristics it already has", animalId);
            return characteristics;
        }

        Animal animalWithUpdatedCharacteristics = AnimalBuilder.copyOf(animal).with(characteristics).build();
        return animalRepository.save(animalWithUpdatedCharacteristics).getCharacteristics().orElseThrow();
    }

    public Characteristics getBy(final UUID animalId) {
        Animal animal = animalRepository.getBy(animalId);
        return animal.getCharacteristics().orElseThrow(EntityNotFoundException::new);
    }
}
