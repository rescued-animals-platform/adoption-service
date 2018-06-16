package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/animals/{animalUuid}/story")
public class StoryResource {

    private final StoryService storyService;

    @Autowired
    public StoryResource(StoryService storyService) {
        this.storyService = storyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Story create(@PathVariable("animalUuid") UUID animalUuid, @RequestBody @Valid Story story) {
        story.setAnimalUuid(animalUuid);
        return storyService.create(story);
    }
}
