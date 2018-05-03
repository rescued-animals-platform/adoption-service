package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaAnimal;
import org.springframework.data.repository.CrudRepository;

public interface JpaAnimalRepository extends CrudRepository<JpaAnimal, Long> {
}
