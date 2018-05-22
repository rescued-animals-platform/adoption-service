package ec.animal.adoption.resources;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.services.CharacteristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CharacteristicsResource {

    private final CharacteristicsService characteristicsService;

    @Autowired
    public CharacteristicsResource(CharacteristicsService characteristicsService) {
        this.characteristicsService = characteristicsService;
    }

    @RequestMapping(path = "/animals/{animalUuid}/characteristics", method = RequestMethod.GET)
    public Characteristics get(@PathVariable("animalUuid") UUID animalUuid) throws EntityNotFoundException {
        return characteristicsService.get(animalUuid);
    }
}
