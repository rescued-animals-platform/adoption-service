package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.Animal;

public interface AnimalRepository {
    Animal save(Animal animal);
}
