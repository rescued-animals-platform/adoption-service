package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.services.AnimalService;

public class AnimalResource {
    private final AnimalService animalService;

    public AnimalResource(AnimalService animalService) {
        this.animalService = animalService;
    }

    public Animal create(Animal animal) {
        return animalService.create(animal);
    }
}
