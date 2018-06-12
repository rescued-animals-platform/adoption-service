package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.Link;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;

public interface MediaLinkRepository {
    Link save(Link media) throws EntityAlreadyExistsException;
}
