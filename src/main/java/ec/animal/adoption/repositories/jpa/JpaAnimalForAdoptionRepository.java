package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaAnimalForAdoption;
import org.springframework.data.repository.CrudRepository;

public interface JpaAnimalForAdoptionRepository extends CrudRepository<JpaAnimalForAdoption, String> {
}
