package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import ec.animal.adoption.repositories.jpa.JpaCharacteristicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CharacteristicsRepositoryPsql implements CharacteristicsRepository {

    private final JpaCharacteristicsRepository jpaCharacteristicsRepository;
    private final AnimalRepositoryPsql animalRepositoryPsql;

    @Autowired
    public CharacteristicsRepositoryPsql(
            JpaCharacteristicsRepository jpaCharacteristicsRepository, AnimalRepositoryPsql animalRepositoryPsql
    ) {
        this.jpaCharacteristicsRepository = jpaCharacteristicsRepository;
        this.animalRepositoryPsql = animalRepositoryPsql;
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
        if(!animalRepositoryPsql.animalExists(characteristics.getAnimalUuid())) {
            throw new EntityNotFoundException();
        }

        try {
            JpaCharacteristics entity = new JpaCharacteristics(characteristics);
            JpaCharacteristics jpaCharacteristics = jpaCharacteristicsRepository.save(entity);
            return jpaCharacteristics.toCharacteristics();
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
