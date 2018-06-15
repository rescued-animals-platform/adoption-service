package ec.animal.adoption.domain.media;

import java.util.UUID;

public class ImagePicture implements Picture {

    private final UUID animalUuid;
    private final String name;
    private final PictureType pictureType;
    private final Image largeImage;
    private final Image smallImage;

    public ImagePicture(UUID animalUuid, String name, PictureType pictureType, Image largeImage, Image smallImage) {
        this.animalUuid = animalUuid;
        this.name = name;
        this.pictureType = pictureType;
        this.largeImage = largeImage;
        this.smallImage = smallImage;
    }

    public UUID getAnimalUuid() {
        return animalUuid;
    }

    public String getName() {
        return name;
    }

    public PictureType getPictureType() {
        return pictureType;
    }

    private String getLargeImageName() {
        return this.name + "_LARGE." + largeImage.getExtension();
    }

    public String getLargeImagePath() {
        return this.animalUuid + "/" + this.getLargeImageName();
    }

    public byte[] getLargeImageContent() {
        return this.largeImage.getContent();
    }

    private String getSmallImageName() {
        return this.name + "_SMALL." + smallImage.getExtension();
    }

    public String getSmallImagePath() {
        return this.animalUuid + "/" + this.getSmallImageName();
    }

    public byte[] getSmallImageContent() {
        return this.smallImage.getContent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImagePicture that = (ImagePicture) o;

        if (animalUuid != null ? !animalUuid.equals(that.animalUuid) : that.animalUuid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pictureType != that.pictureType) return false;
        if (largeImage != null ? !largeImage.equals(that.largeImage) : that.largeImage != null) return false;
        return smallImage != null ? smallImage.equals(that.smallImage) : that.smallImage == null;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pictureType != null ? pictureType.hashCode() : 0);
        result = 31 * result + (largeImage != null ? largeImage.hashCode() : 0);
        result = 31 * result + (smallImage != null ? smallImage.hashCode() : 0);
        return result;
    }
}
