package ec.animal.adoption.services;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.repositories.CharacteristicsRepository;

import java.util.UUID;

public class CharacteristicsService {

    private final CharacteristicsRepository characteristicsRepository;

    public CharacteristicsService(CharacteristicsRepository characteristicsRepository) {
        this.characteristicsRepository = characteristicsRepository;
    }

    public Characteristics get(UUID animalUuid) {
        return characteristicsRepository.getBy(animalUuid);
    }
}
