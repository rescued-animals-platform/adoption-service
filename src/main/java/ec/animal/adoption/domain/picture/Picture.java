package ec.animal.adoption.domain.picture;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.InputStream;
import java.util.Optional;

public class Picture {

    private final String name;
    private final String filename;
    private final PictureRepresentation pictureRepresentation;

    @JsonCreator
    public Picture(
            @JsonProperty("name") String name,
            @JsonProperty("filename") String filename,
            @JsonProperty("mediaLink") PictureRepresentation pictureRepresentation
    ) {
        this.name = name;
        this.filename = filename;
        this.pictureRepresentation = pictureRepresentation;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public Optional<InputStream> getInputStream() {
        if(pictureRepresentation instanceof Image) {
            return Optional.of(((Image) pictureRepresentation).getInputStream());
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        if (name != null ? !name.equals(picture.name) : picture.name != null) return false;
        if (filename != null ? !filename.equals(picture.filename) : picture.filename != null) return false;
        return pictureRepresentation != null ? pictureRepresentation.equals(picture.pictureRepresentation) : picture.pictureRepresentation == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (pictureRepresentation != null ? pictureRepresentation.hashCode() : 0);
        return result;
    }
}
