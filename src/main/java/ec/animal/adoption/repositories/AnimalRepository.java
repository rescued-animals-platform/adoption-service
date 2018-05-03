package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;

public interface AnimalRepository {
    Animal save(Animal animal) throws EntityAlreadyExistsException;
}
