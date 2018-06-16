package ec.animal.adoption.services;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.repositories.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryService {

    private final StoryRepository storyRepository;

    @Autowired
    public StoryService(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Story create(Story story) {
        return storyRepository.save(story);
    }
}
