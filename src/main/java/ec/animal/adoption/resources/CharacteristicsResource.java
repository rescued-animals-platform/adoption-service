package ec.animal.adoption.resources;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.services.CharacteristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/animals/{animalUuid}/characteristics")
public class CharacteristicsResource {

    private final CharacteristicsService characteristicsService;

    @Autowired
    public CharacteristicsResource(CharacteristicsService characteristicsService) {
        this.characteristicsService = characteristicsService;
    }

    @GetMapping
    public Characteristics get(@PathVariable("animalUuid") UUID animalUuid) throws EntityNotFoundException {
        return characteristicsService.get(animalUuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Characteristics create(
            @PathVariable("animalUuid") UUID animalUuid, @RequestBody @Valid Characteristics characteristics
    ) throws EntityAlreadyExistsException {
        return characteristicsService.create(animalUuid, characteristics);
    }
}
