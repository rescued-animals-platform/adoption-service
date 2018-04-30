package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.JpaAnimalForAdoption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnimalForAdoptionRepository extends JpaRepository<JpaAnimalForAdoption, String> {
}
