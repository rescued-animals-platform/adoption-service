package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AnimalResource {

    private final AnimalService animalService;

    @Autowired
    public AnimalResource(AnimalService animalService) {
        this.animalService = animalService;
    }

    @RequestMapping(path = "/animals", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Animal create(@RequestBody @Valid Animal animal) throws EntityAlreadyExistsException {
        return animalService.create(animal);
    }
}
