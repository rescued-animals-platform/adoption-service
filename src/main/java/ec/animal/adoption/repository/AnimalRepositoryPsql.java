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

package ec.animal.adoption.repository;

import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalRepository;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.exception.EntityAlreadyExistsException;
import ec.animal.adoption.domain.exception.EntityNotFoundException;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.repository.jpa.JpaAnimalRepository;
import ec.animal.adoption.repository.jpa.model.JpaAnimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AnimalRepositoryPsql implements AnimalRepository {

    private final JpaAnimalRepository jpaAnimalRepository;

    @Autowired
    public AnimalRepositoryPsql(final JpaAnimalRepository jpaAnimalRepository) {
        this.jpaAnimalRepository = jpaAnimalRepository;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public Animal save(final Animal animal) {
        try {
            JpaAnimal savedJpaAnimal = jpaAnimalRepository.save(new JpaAnimal(animal));
            return savedJpaAnimal.toAnimal();
        } catch (Exception exception) {
            throw new EntityAlreadyExistsException(exception);
        }
    }

    @Override
    public boolean exists(final Animal animal) {
        Optional<JpaAnimal> jpaAnimal = jpaAnimalRepository.findByClinicalRecordAndJpaOrganizationUuid(
                animal.getClinicalRecord(), animal.getOrganizationUuid()
        );
        return jpaAnimal.isPresent();
    }

    @Override
    public Animal getBy(final UUID animalUuid) {
        Optional<JpaAnimal> jpaAnimal = jpaAnimalRepository.findById(animalUuid);
        return jpaAnimal.map(JpaAnimal::toAnimal).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Animal getBy(final UUID animalUuid, final Organization organization) {
        Optional<JpaAnimal> jpaAnimal = jpaAnimalRepository.findByUuidAndJpaOrganizationUuid(
                animalUuid, organization.getOrganizationUuid()
        );
        return jpaAnimal.map(JpaAnimal::toAnimal).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public PagedEntity<Animal> getAllFor(final Organization organization, final Pageable pageable) {
        Page<JpaAnimal> jpaAnimals = jpaAnimalRepository.findAllByJpaOrganizationUuid(
                organization.getOrganizationUuid(), pageable
        );
        return new PagedEntity<>(jpaAnimals.map(JpaAnimal::toAnimal));
    }

    @Override
    public PagedEntity<Animal> getAllBy(final String stateName,
                                        final Species species,
                                        final PhysicalActivity physicalActivity,
                                        final Size size,
                                        final Pageable pageable) {
        Page<JpaAnimal> jpaAnimals = jpaAnimalRepository.findAllByStateNameAndSpeciesOrJpaCharacteristicsPhysicalActivityOrJpaCharacteristicsSize(
                stateName, species.name(), physicalActivity.name(), size.name(), pageable
        );
        return new PagedEntity<>(jpaAnimals.map(JpaAnimal::toAnimal));
    }
}
