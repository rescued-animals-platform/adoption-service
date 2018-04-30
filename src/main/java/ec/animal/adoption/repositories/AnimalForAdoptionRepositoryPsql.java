package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.models.JpaAnimalForAdoption;
import ec.animal.adoption.repositories.jpa.JpaAnimalForAdoptionRepository;

public class AnimalForAdoptionRepositoryPsql implements AnimalForAdoptionRepository {

    private final JpaAnimalForAdoptionRepository jpaAnimalForAdoptionRepository;

    public AnimalForAdoptionRepositoryPsql(JpaAnimalForAdoptionRepository jpaAnimalForAdoptionRepository) {
        this.jpaAnimalForAdoptionRepository = jpaAnimalForAdoptionRepository;
    }

    @Override
    public AnimalForAdoption save(AnimalForAdoption animalForAdoption) {
        JpaAnimalForAdoption savedJpaAnimalForAdoption = jpaAnimalForAdoptionRepository.saveAndFlush(
                new JpaAnimalForAdoption(animalForAdoption)
        );
        return savedJpaAnimalForAdoption.toAvailableAnimal();
    }
}
