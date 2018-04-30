package ec.animal.adoption.services;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.repositories.AnimalForAdoptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalForAdoptionService {
    private final AnimalForAdoptionRepository animalForAdoptionRepository;

    @Autowired
    public AnimalForAdoptionService(AnimalForAdoptionRepository animalForAdoptionRepository) {
        this.animalForAdoptionRepository = animalForAdoptionRepository;
    }

    public AnimalForAdoption create(AnimalForAdoption animalForAdoption) {
        return animalForAdoptionRepository.save(animalForAdoption);
    }
}
