package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaStory;
import org.springframework.data.repository.CrudRepository;

public interface JpaStoryRepository extends CrudRepository<JpaStory, Long> {
}
