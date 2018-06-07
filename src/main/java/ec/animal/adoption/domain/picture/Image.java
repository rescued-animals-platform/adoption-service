package ec.animal.adoption.domain.picture;

import ec.animal.adoption.domain.validators.ValidImage;

import java.io.InputStream;
import java.util.Arrays;

@ValidImage
public class Image implements PictureRepresentation {

    private final InputStream image;
    private final byte[] bytes;

    public Image(InputStream image, byte[] bytes) {
        this.image = image;
        this.bytes = bytes;
    }

    InputStream getInputStream() {
        return image;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (this.image != null ? !this.image.equals(image.image) : image.image != null) return false;
        return Arrays.equals(bytes, image.bytes);
    }

    @Override
    public int hashCode() {
        int result = image != null ? image.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}
