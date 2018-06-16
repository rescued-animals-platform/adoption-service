package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaStory;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import ec.animal.adoption.repositories.jpa.JpaStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StoryRepositoryPsql implements StoryRepository {

    private final JpaStoryRepository jpaStoryRepository;
    private final JpaAnimalRepository jpaAnimalRepository;

    @Autowired
    public StoryRepositoryPsql(JpaStoryRepository jpaStoryRepository, JpaAnimalRepository jpaAnimalRepository) {
        this.jpaStoryRepository = jpaStoryRepository;
        this.jpaAnimalRepository = jpaAnimalRepository;
    }

    @Override
    public Story save(Story story) {
        Optional<JpaAnimal> jpaAnimal = jpaAnimalRepository.findById(story.getAnimalUuid());
        if(!jpaAnimal.isPresent()) {
            throw new EntityNotFoundException();
        }

        try {
            JpaStory jpaStory = jpaStoryRepository.save(new JpaStory(story));
            return jpaStory.toStory();
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
