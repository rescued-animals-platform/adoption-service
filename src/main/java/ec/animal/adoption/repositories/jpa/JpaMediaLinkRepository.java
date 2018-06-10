package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaMediaLink;
import ec.animal.adoption.models.jpa.JpaMediaLinkId;
import org.springframework.data.repository.CrudRepository;

public interface JpaMediaLinkRepository extends CrudRepository<JpaMediaLink, JpaMediaLinkId> {

}
