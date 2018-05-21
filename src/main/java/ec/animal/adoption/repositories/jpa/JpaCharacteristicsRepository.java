package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaCharacteristics;

import java.util.UUID;

public interface JpaCharacteristicsRepository {
    JpaCharacteristics findByAnimalUuid(UUID animalUuid);
}
