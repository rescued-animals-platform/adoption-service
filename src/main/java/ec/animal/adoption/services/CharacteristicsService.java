package ec.animal.adoption.services;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.repositories.CharacteristicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CharacteristicsService {

    private final CharacteristicsRepository characteristicsRepository;

    @Autowired
    public CharacteristicsService(CharacteristicsRepository characteristicsRepository) {
        this.characteristicsRepository = characteristicsRepository;
    }

    public Characteristics create(UUID animalUuid, Characteristics characteristics) {
        characteristics.setAnimalUuid(animalUuid);
        return characteristicsRepository.save(characteristics);
    }

    public Characteristics get(UUID animalUuid) {
        return characteristicsRepository.getBy(animalUuid);
    }
}
