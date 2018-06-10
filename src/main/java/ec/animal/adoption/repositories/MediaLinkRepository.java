package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;

public interface MediaLinkRepository {
    MediaLink save(MediaLink media) throws EntityAlreadyExistsException;
}
