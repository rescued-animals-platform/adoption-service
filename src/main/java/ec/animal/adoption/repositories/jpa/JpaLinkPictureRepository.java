package ec.animal.adoption.repositories.jpa;

import ec.animal.adoption.models.jpa.JpaLinkPicture;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaLinkPictureRepository extends CrudRepository<JpaLinkPicture, Long> {

    Optional<JpaLinkPicture> findByPictureTypeAndAnimalUuid(String pictureType, UUID animalUuid);
}
