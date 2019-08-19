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

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.dtos.AnimalDto;
import ec.animal.adoption.exceptions.InvalidStateException;
import ec.animal.adoption.repositories.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalService(final AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal create(final Animal animal) {
        return animalRepository.save(animal);
    }

    public Animal getBy(final UUID uuid) {
        return animalRepository.getBy(uuid);
    }

    public PagedEntity<AnimalDto> listAllByStateWithPagination(final String stateName, final Pageable pageable) {
        if (!State.isValidStateName(stateName)) {
            throw new InvalidStateException(stateName);
        }

        return animalRepository.getAllBy(stateName, pageable)
                .map(a -> new AnimalDto(a.getUuid(), a.getName(), a.getSpecies(), a.getEstimatedAge(), a.getSex()));
    }
}
