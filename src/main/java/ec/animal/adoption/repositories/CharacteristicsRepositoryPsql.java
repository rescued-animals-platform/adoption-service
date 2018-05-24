package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import ec.animal.adoption.repositories.jpa.JpaCharacteristicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CharacteristicsRepositoryPsql implements CharacteristicsRepository {

    private JpaCharacteristicsRepository jpaCharacteristicsRepository;

    @Autowired
    public CharacteristicsRepositoryPsql(JpaCharacteristicsRepository jpaCharacteristicsRepository) {
        this.jpaCharacteristicsRepository = jpaCharacteristicsRepository;
    }

    @Override
    public Characteristics getBy(UUID animalUuid) throws EntityNotFoundException {
        JpaCharacteristics jpaCharacteristics = jpaCharacteristicsRepository.findByAnimalUuid(animalUuid);

        if(jpaCharacteristics == null) {
            throw new EntityNotFoundException();
        }

        return jpaCharacteristics.toCharacteristics();
    }

    @Override
    public Characteristics save(Characteristics characteristics) throws EntityAlreadyExistsException {
        try {
            JpaCharacteristics jpaCharacteristics = jpaCharacteristicsRepository.save(
                    new JpaCharacteristics(characteristics)
            );
            return jpaCharacteristics.toCharacteristics();
        } catch(Exception ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
