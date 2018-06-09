package ec.animal.adoption.domain.media;

import ec.animal.adoption.domain.validators.ValidImageMedia;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.UUID;

@ValidImageMedia
public class ImageMedia {

    private final UUID animalUuid;
    private final String extension;
    private final byte[] content;

    @Min(value = 1, message = "Image media size in bytes can't be zero")
    private final long sizeInBytes;

    public ImageMedia(UUID animalUuid, String extension, byte[] content, long sizeInBytes) {
        this.animalUuid = animalUuid;
        this.extension = extension;
        this.content = content;
        this.sizeInBytes = sizeInBytes;
    }

    public UUID getAnimalUuid() {
        return animalUuid;
    }

    public String getExtension() {
        return extension;
    }

    public String getName() {
        return "PrimaryImageLarge." + this.extension;
    }

    public String getPath() {
        return animalUuid + "/" + this.getName();
    }

    public byte[] getContent() {
        return content;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageMedia that = (ImageMedia) o;

        if (sizeInBytes != that.sizeInBytes) return false;
        if (animalUuid != null ? !animalUuid.equals(that.animalUuid) : that.animalUuid != null) return false;
        if (extension != null ? !extension.equals(that.extension) : that.extension != null) return false;
        return Arrays.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(content);
        result = 31 * result + (int) (sizeInBytes ^ (sizeInBytes >>> 32));
        return result;
    }
}
