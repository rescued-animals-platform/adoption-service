package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.MediaLink;

public interface MediaLinkRepository {
    MediaLink save(MediaLink media);
}
