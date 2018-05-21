package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaAnimal;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface JpaAnimalRepository extends CrudRepository<JpaAnimal, UUID> {
}
