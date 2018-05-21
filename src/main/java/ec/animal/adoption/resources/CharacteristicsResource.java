package ec.animal.adoption.resources;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.services.CharacteristicsService;

import java.util.UUID;

public class CharacteristicsResource {

    private final CharacteristicsService characteristicsService;

    public CharacteristicsResource(CharacteristicsService characteristicsService) {
        this.characteristicsService = characteristicsService;
    }

    public Characteristics get(UUID animalUuid) {
        return characteristicsService.get(animalUuid);
    }
}
