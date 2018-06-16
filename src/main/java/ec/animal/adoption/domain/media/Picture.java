package ec.animal.adoption.domain.media;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public abstract class Picture {

    protected UUID animalUuid;
    protected String name;
    protected PictureType pictureType;

    public UUID getAnimalUuid() {
        return animalUuid;
    }

    public String getName() {
        return name;
    }

    public PictureType getPictureType() {
        return pictureType;
    }

    @JsonIgnore
    public boolean hasUrls() {
        return false;
    }

    @JsonIgnore
    public String getLargeImageUrl() {
        return null;
    };

    @JsonIgnore
    public String getSmallImageUrl() {
        return null;
    }

    @JsonIgnore
    public boolean hasImages() {
        return false;
    }

    @JsonIgnore
    public String getLargeImagePath() {
        return null;
    }

    @JsonIgnore
    public byte[] getLargeImageContent() {
        return null;
    }

    @JsonIgnore
    public String getSmallImagePath() {
        return null;
    }

    @JsonIgnore
    public byte[] getSmallImageContent() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        if (animalUuid != null ? !animalUuid.equals(picture.animalUuid) : picture.animalUuid != null) return false;
        if (name != null ? !name.equals(picture.name) : picture.name != null) return false;
        return pictureType == picture.pictureType;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pictureType != null ? pictureType.hashCode() : 0);
        return result;
    }
}
