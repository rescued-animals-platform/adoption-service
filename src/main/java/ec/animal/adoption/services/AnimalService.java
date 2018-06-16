package ec.animal.adoption.services;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.repositories.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal create(Animal animal) {
        return animalRepository.save(animal);
    }
}
