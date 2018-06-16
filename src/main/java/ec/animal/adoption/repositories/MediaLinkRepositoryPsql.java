package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.jpa.JpaMediaLink;
import ec.animal.adoption.repositories.jpa.JpaMediaLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MediaLinkRepositoryPsql implements MediaLinkRepository {

    private final JpaMediaLinkRepository jpaMediaLinkRepository;

    @Autowired
    public MediaLinkRepositoryPsql(JpaMediaLinkRepository jpaMediaLinkRepository) {
        this.jpaMediaLinkRepository = jpaMediaLinkRepository;
    }

    @Override
    public MediaLink save(MediaLink mediaLink) throws EntityAlreadyExistsException {
        try {
            JpaMediaLink jpaMediaLink = jpaMediaLinkRepository.save(new JpaMediaLink(mediaLink));
            return jpaMediaLink.toMediaLink();
        } catch (Exception ex) {
            throw new EntityAlreadyExistsException();
        }
    }
}
