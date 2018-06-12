package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaImage;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface JpaImageRepository extends CrudRepository<JpaImage, UUID> {

}
