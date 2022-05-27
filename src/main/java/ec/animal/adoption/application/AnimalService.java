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
import ec.animal.adoption.domain.model.animal.Species;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.model.characteristics.Size;
import ec.animal.adoption.domain.model.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.model.exception.IllegalUpdateException;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.state.StateName;
import ec.animal.adoption.domain.service.AnimalRepository;
import ec.animal.adoption.domain.service.PagedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalService(final AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal create(final AnimalDto animalDto) {
        if (animalRepository.exists(animalDto.getClinicalRecord(), animalDto.getOrganizationId())) {
            throw new EntityAlreadyExistsException();
        }

        return animalRepository.create(animalDto);
    }

    public Animal update(final UUID animalId, final AnimalDto animalDto) {
        Organization organization = animalDto.getOrganization();
        String clinicalRecordInUpdateRequest = animalDto.getClinicalRecord();

        Animal animalToBeUpdated = animalRepository.getBy(animalId, organization);
        Optional<Animal> animalInOrganizationWithClinicalRecordInUpdate = animalRepository.getBy(
                clinicalRecordInUpdateRequest, organization
        );

        animalInOrganizationWithClinicalRecordInUpdate.ifPresent(animal -> {
            if (!animalToBeUpdated.isSameAs(animal)) {
                throw new IllegalUpdateException(animal.getIdentifier(), clinicalRecordInUpdateRequest);
            }
        });

        return animalRepository.save(animalToBeUpdated.updateWith(animalDto));
    }

    public Animal getBy(final UUID animalId, final Organization organization) {
        return animalRepository.getBy(animalId, organization);
    }

    public PagedEntity<Animal> listAllFor(final Organization organization, final Pageable pageable) {
        return animalRepository.getAllFor(organization, pageable);
    }

    public PagedEntity<Animal> listAllWithFilters(final StateName stateName,
                                                  final Species species,
                                                  final PhysicalActivity physicalActivity,
                                                  final Size size,
                                                  final Pageable pageable) {
        return animalRepository.getAllBy(stateName.name(), species, physicalActivity, size, pageable);
    }
}
