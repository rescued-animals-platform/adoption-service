package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.jpa.JpaAnimalForAdoption;
import ec.animal.adoption.repositories.jpa.JpaAnimalForAdoptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalForAdoptionRepositoryPsql implements AnimalForAdoptionRepository {

    private final JpaAnimalForAdoptionRepository jpaAnimalForAdoptionRepository;

    @Autowired
    public AnimalForAdoptionRepositoryPsql(JpaAnimalForAdoptionRepository jpaAnimalForAdoptionRepository) {
        this.jpaAnimalForAdoptionRepository = jpaAnimalForAdoptionRepository;
    }

    @Override
    public AnimalForAdoption save(AnimalForAdoption animalForAdoption) throws EntityAlreadyExistsException {
        try {
            JpaAnimalForAdoption savedJpaAnimalForAdoption = jpaAnimalForAdoptionRepository.save(
                    new JpaAnimalForAdoption(animalForAdoption)
            );

            return savedJpaAnimalForAdoption.toAvailableAnimal();
        } catch (Exception ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
