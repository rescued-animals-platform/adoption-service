package ec.animal.adoption.domain.picture;

public class Picture {

    private final String name;
    private final PictureRepresentation pictureRepresentation;

    public Picture(String name, PictureRepresentation pictureRepresentation) {
        this.name = name;
        this.pictureRepresentation = pictureRepresentation;
    }

    public String getName() {
        return name;
    }

    public PictureRepresentation getPictureRepresentation() {
        return pictureRepresentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        if (name != null ? !name.equals(picture.name) : picture.name != null) return false;
        return pictureRepresentation != null ? pictureRepresentation.equals(picture.pictureRepresentation) : picture.pictureRepresentation == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (pictureRepresentation != null ? pictureRepresentation.hashCode() : 0);
        return result;
    }
}
