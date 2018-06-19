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

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class AnimalRepositoryPsql implements AnimalRepository {

    private final JpaAnimalRepository jpaAnimalRepository;

    @Autowired
    public AnimalRepositoryPsql(JpaAnimalRepository jpaAnimalRepository) {
        this.jpaAnimalRepository = jpaAnimalRepository;
    }

    @Override
    public Animal save(Animal animal) {
        try {
            JpaAnimal savedJpaAnimal = jpaAnimalRepository.save(new JpaAnimal(animal));
            return savedJpaAnimal.toAnimal();
        } catch (Exception ex) {
            throw new EntityAlreadyExistsException();
        }
    }

    public boolean animalExists(UUID animalUuid) {
        return jpaAnimalRepository.findById(animalUuid).isPresent();
    }
}
