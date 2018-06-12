package ec.animal.adoption.domain.media;

import java.util.UUID;

public class LargeImageMetadata implements MediaMetadata {

    private final UUID animalUuid;
    private final String name;
    private final ImageType imageType;

    public LargeImageMetadata(UUID animalUuid, String name, ImageType imageType) {
        this.animalUuid = animalUuid;
        this.name = name;
        this.imageType = imageType;
    }

    public UUID getAnimalUuid() {
        return this.animalUuid;
    }

    public ImageType getImageType() {
        return this.imageType;
    }

    public String getName() {
        return this.name;
    }
}
