package ec.animal.adoption.resources;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.services.AnimalForAdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AnimalForAdoptionResource {
    private final AnimalForAdoptionService animalForAdoptionService;

    @Autowired
    public AnimalForAdoptionResource(AnimalForAdoptionService animalForAdoptionService) {
        this.animalForAdoptionService = animalForAdoptionService;
    }

    @RequestMapping(path = "/animals", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalForAdoption create(@RequestBody @Valid AnimalForAdoption animalForAdoption) throws EntityAlreadyExistsException {
        return animalForAdoptionService.create(animalForAdoption);
    }
}
