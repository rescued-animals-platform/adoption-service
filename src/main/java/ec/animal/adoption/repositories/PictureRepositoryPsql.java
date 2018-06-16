package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.jpa.JpaLinkPicture;
import ec.animal.adoption.repositories.jpa.JpaLinkPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class PictureRepositoryPsql implements PictureRepository {

    private final JpaLinkPictureRepository jpaLinkPictureRepository;

    @Autowired
    public PictureRepositoryPsql(JpaLinkPictureRepository jpaLinkPictureRepository) {
        this.jpaLinkPictureRepository = jpaLinkPictureRepository;
    }

    @Override
    public Picture save(Picture picture) {
        try {
            JpaLinkPicture jpaLinkPicture = jpaLinkPictureRepository.save(new JpaLinkPicture(picture));
            return jpaLinkPicture.toPicture();
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
