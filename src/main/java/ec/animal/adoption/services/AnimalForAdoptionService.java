package ec.animal.adoption.services;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.repositories.AnimalForAdoptionRepository;

public class AnimalForAdoptionService {
    private final AnimalForAdoptionRepository animalForAdoptionRepository;

    public AnimalForAdoptionService(AnimalForAdoptionRepository animalForAdoptionRepository) {
        this.animalForAdoptionRepository = animalForAdoptionRepository;
    }

    public AnimalForAdoption create(AnimalForAdoption animalForAdoption) {
        return animalForAdoptionRepository.save(animalForAdoption);
    }
}
