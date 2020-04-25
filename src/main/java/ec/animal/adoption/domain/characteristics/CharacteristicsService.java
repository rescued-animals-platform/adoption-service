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

package ec.animal.adoption.domain.characteristics;

import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalRepository;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.organization.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CharacteristicsService {

    private final AnimalRepository animalRepository;

    @Autowired
    public CharacteristicsService(final AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Characteristics createFor(final UUID animalUuid,
                                     final Organization organization,
                                     final Characteristics characteristics) {
        Animal animal = animalRepository.getBy(animalUuid, organization);
        animal.getCharacteristics().ifPresent(c -> {
            throw new EntityAlreadyExistsException();
        });

        animal.setCharacteristics(characteristics);
        return animalRepository.save(animal).getCharacteristics().orElseThrow();
    }

    public Characteristics getBy(final UUID animalUuid) {
        Animal animal = animalRepository.getBy(animalUuid);
        return animal.getCharacteristics().orElseThrow(EntityNotFoundException::new);
    }
}
