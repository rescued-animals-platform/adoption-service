package ec.animal.adoption.resources;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.services.AnimalForAdoptionService;

public class AnimalForAdoptionResource {
    private final AnimalForAdoptionService animalForAdoptionService;

    public AnimalForAdoptionResource(AnimalForAdoptionService animalForAdoptionService) {
        this.animalForAdoptionService = animalForAdoptionService;
    }

    public AnimalForAdoption create(AnimalForAdoption animalForAdoption) {
        return animalForAdoptionService.create(animalForAdoption);
    }
}
