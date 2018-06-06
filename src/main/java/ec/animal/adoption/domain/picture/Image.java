package ec.animal.adoption.domain.picture;

import java.io.InputStream;

public class Image implements PictureRepresentation {

    private final InputStream image;

    public Image(InputStream image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image1 = (Image) o;

        return image != null ? image.equals(image1.image) : image1.image == null;
    }

    @Override
    public int hashCode() {
        return image != null ? image.hashCode() : 0;
    }
}
