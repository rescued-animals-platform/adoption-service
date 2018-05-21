package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import ec.animal.adoption.repositories.jpa.JpaCharacteristicsRepository;

import java.util.UUID;

public class CharacteristicsRepositoryPsql implements CharacteristicsRepository {

    private JpaCharacteristicsRepository jpaCharacteristicsRepository;

    public CharacteristicsRepositoryPsql(JpaCharacteristicsRepository jpaCharacteristicsRepository) {
        this.jpaCharacteristicsRepository = jpaCharacteristicsRepository;
    }

    @Override
    public Characteristics getBy(UUID animalUuid) {
        JpaCharacteristics jpaCharacteristics = jpaCharacteristicsRepository.findByAnimalUuid(animalUuid);
        return jpaCharacteristics.toCharacteristics();
    }
}
