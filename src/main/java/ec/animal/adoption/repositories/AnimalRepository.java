package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.Animal;

import java.util.UUID;

public interface AnimalRepository {
    Animal save(Animal animal);

    boolean animalExists(UUID animalUuid);
}
