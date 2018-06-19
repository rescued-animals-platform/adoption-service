package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaLinkPicture;
import ec.animal.adoption.repositories.jpa.JpaLinkPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PictureRepositoryPsql implements PictureRepository {

    private final JpaLinkPictureRepository jpaLinkPictureRepository;
    private final AnimalRepositoryPsql animalRepositoryPsql;

    @Autowired
    public PictureRepositoryPsql(
            JpaLinkPictureRepository jpaLinkPictureRepository, AnimalRepositoryPsql animalRepositoryPsql
    ) {
        this.jpaLinkPictureRepository = jpaLinkPictureRepository;
        this.animalRepositoryPsql = animalRepositoryPsql;
    }

    @Override
    public Picture save(Picture picture) {
        UUID animalUuid = picture.getAnimalUuid();

        if(!animalRepositoryPsql.animalExists(animalUuid)) {
            throw new EntityNotFoundException();
        }

        if(primaryLinkPictureExists(animalUuid)) {
            throw new EntityAlreadyExistsException();
        }

        return saveJpaLinkPicture(new JpaLinkPicture(picture)).toPicture();
    }

    private boolean primaryLinkPictureExists(UUID animalUuid) {
        Optional<JpaLinkPicture> jpaPrimaryLinkPicture = jpaLinkPictureRepository.findByPictureTypeAndAnimalUuid(
                PictureType.PRIMARY.name(), animalUuid
        );
        return jpaPrimaryLinkPicture.isPresent();
    }

    private JpaLinkPicture saveJpaLinkPicture(JpaLinkPicture jpaLinkPicture) {
        try {
            return jpaLinkPictureRepository.save(jpaLinkPicture);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
