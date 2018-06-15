package ec.animal.adoption.domain.media;

import ec.animal.adoption.domain.validators.ValidImage;

import javax.validation.constraints.Min;
import java.util.Arrays;

@ValidImage
public class Image {
    private final String extension;
    private final byte[] content;

    @Min(value = 1, message = "Image size in bytes can't be zero")
    private final long sizeInBytes;

    public Image(String extension, byte[] content, long sizeInBytes) {

        this.extension = extension;
        this.content = content;
        this.sizeInBytes = sizeInBytes;
    }

    public String getExtension() {
        return extension;
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

        Image image = (Image) o;

        if (sizeInBytes != image.sizeInBytes) return false;
        if (extension != null ? !extension.equals(image.extension) : image.extension != null) return false;
        return Arrays.equals(content, image.content);
    }

    @Override
    public int hashCode() {
        int result = extension != null ? extension.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(content);
        result = 31 * result + (int) (sizeInBytes ^ (sizeInBytes >>> 32));
        return result;
    }
}
