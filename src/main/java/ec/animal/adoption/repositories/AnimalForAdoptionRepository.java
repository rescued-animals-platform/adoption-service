package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;

public interface AnimalForAdoptionRepository {
    AnimalForAdoption save(AnimalForAdoption animalForAdoption) throws EntityAlreadyExistsException;
}
