package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaLinkPicture;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import ec.animal.adoption.repositories.jpa.JpaLinkPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PictureRepositoryPsql implements PictureRepository {

    private final JpaLinkPictureRepository jpaLinkPictureRepository;
    private final JpaAnimalRepository jpaAnimalRepository;

    @Autowired
    public PictureRepositoryPsql(
            JpaLinkPictureRepository jpaLinkPictureRepository, JpaAnimalRepository jpaAnimalRepository
    ) {
        this.jpaLinkPictureRepository = jpaLinkPictureRepository;
        this.jpaAnimalRepository = jpaAnimalRepository;
    }

    @Override
    public Picture save(Picture picture) {
        Optional<JpaAnimal> jpaAnimal = jpaAnimalRepository.findById(picture.getAnimalUuid());
        if(!jpaAnimal.isPresent()) {
            throw new EntityNotFoundException();
        }

        try {
            JpaLinkPicture jpaLinkPicture = jpaLinkPictureRepository.save(new JpaLinkPicture(picture));
            return jpaLinkPicture.toPicture();
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
