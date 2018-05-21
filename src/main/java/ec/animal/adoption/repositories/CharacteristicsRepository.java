package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.characteristics.Characteristics;

import java.util.UUID;

public interface CharacteristicsRepository {
    Characteristics getBy(UUID animalUuid);
}
