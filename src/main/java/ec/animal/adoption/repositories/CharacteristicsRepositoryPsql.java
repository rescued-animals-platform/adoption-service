package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import ec.animal.adoption.repositories.jpa.JpaCharacteristicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CharacteristicsRepositoryPsql implements CharacteristicsRepository {

    private final JpaCharacteristicsRepository jpaCharacteristicsRepository;
    private final JpaAnimalRepository jpaAnimalRepository;

    @Autowired
    public CharacteristicsRepositoryPsql(
            JpaCharacteristicsRepository jpaCharacteristicsRepository, JpaAnimalRepository jpaAnimalRepository
    ) {
        this.jpaCharacteristicsRepository = jpaCharacteristicsRepository;
        this.jpaAnimalRepository = jpaAnimalRepository;
    }

    @Override
    public Characteristics getBy(UUID animalUuid) {
        Optional<JpaCharacteristics> jpaCharacteristics = jpaCharacteristicsRepository.findByAnimalUuid(animalUuid);

        if (!jpaCharacteristics.isPresent()) {
            throw new EntityNotFoundException();
        }

        return jpaCharacteristics.get().toCharacteristics();
    }

    @Override
    public Characteristics save(Characteristics characteristics) {
        Optional<JpaAnimal> jpaAnimal = jpaAnimalRepository.findById(characteristics.getAnimalUuid());
        if(!jpaAnimal.isPresent()) {
            throw new EntityNotFoundException();
        }

        try {
            JpaCharacteristics jpaCharacteristics = jpaCharacteristicsRepository.save(
                    new JpaCharacteristics(characteristics)
            );
            return jpaCharacteristics.toCharacteristics();
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
