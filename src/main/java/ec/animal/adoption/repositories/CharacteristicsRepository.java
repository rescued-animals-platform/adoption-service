package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;

import java.util.UUID;

public interface CharacteristicsRepository {

    Characteristics getBy(UUID animalUuid) throws EntityNotFoundException;

    Characteristics save(Characteristics characteristics) throws EntityAlreadyExistsException;
}
