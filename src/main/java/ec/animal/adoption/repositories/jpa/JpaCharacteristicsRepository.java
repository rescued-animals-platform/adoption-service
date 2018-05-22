package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaCharacteristics;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface JpaCharacteristicsRepository extends CrudRepository<JpaCharacteristics, Long> {

    JpaCharacteristics findByAnimalUuid(UUID animalUuid);
}
