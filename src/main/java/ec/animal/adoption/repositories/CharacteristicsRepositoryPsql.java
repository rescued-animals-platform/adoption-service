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

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import ec.animal.adoption.repositories.jpa.JpaCharacteristicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CharacteristicsRepositoryPsql implements CharacteristicsRepository {

    private final JpaCharacteristicsRepository jpaCharacteristicsRepository;
    private final AnimalRepositoryPsql animalRepositoryPsql;

    @Autowired
    public CharacteristicsRepositoryPsql(
            JpaCharacteristicsRepository jpaCharacteristicsRepository, AnimalRepositoryPsql animalRepositoryPsql
    ) {
        this.jpaCharacteristicsRepository = jpaCharacteristicsRepository;
        this.animalRepositoryPsql = animalRepositoryPsql;
    }

    @Override
    public Characteristics save(Characteristics characteristics) {
        if(!animalRepositoryPsql.animalExists(characteristics.getAnimalUuid())) {
            throw new EntityNotFoundException();
        }

        try {
            JpaCharacteristics entity = new JpaCharacteristics(characteristics);
            JpaCharacteristics jpaCharacteristics = jpaCharacteristicsRepository.save(entity);
            return jpaCharacteristics.toCharacteristics();
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }

    @Override
    public Characteristics getBy(UUID animalUuid) {
        Optional<JpaCharacteristics> jpaCharacteristics = jpaCharacteristicsRepository.findByAnimalUuid(animalUuid);

        if (!jpaCharacteristics.isPresent()) {
            throw new EntityNotFoundException();
        }

        return jpaCharacteristics.get().toCharacteristics();
    }
}
