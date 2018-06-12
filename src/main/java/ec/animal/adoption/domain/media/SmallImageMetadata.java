package ec.animal.adoption.domain.media;

import java.util.UUID;

public class SmallImageMetadata implements MediaMetadata {

    private final UUID largeImageId;

    public SmallImageMetadata(UUID largeImageId) {
        this.largeImageId = largeImageId;
    }

    public UUID getLargeImageId() {
        return this.largeImageId;
    }
}
