package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.AnimalForAdoption;

public interface AnimalForAdoptionRepository {
    AnimalForAdoption save(AnimalForAdoption animalForAdoption);
}
