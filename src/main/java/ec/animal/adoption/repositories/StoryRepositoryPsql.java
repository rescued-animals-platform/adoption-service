package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.Story;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaStory;
import ec.animal.adoption.repositories.jpa.JpaStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class StoryRepositoryPsql implements StoryRepository {

    private final JpaStoryRepository jpaStoryRepository;
    private final AnimalRepositoryPsql animalRepositoryPsql;

    @Autowired
    public StoryRepositoryPsql(JpaStoryRepository jpaStoryRepository, AnimalRepositoryPsql animalRepositoryPsql) {
        this.jpaStoryRepository = jpaStoryRepository;
        this.animalRepositoryPsql = animalRepositoryPsql;
    }

    @Override
    public Story save(Story story) {
        if(!animalRepositoryPsql.animalExists(story.getAnimalUuid())) {
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
