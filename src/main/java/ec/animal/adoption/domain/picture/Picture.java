package ec.animal.adoption.domain.picture;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.validators.ValidFilename;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.InputStream;
import java.util.Optional;

public class Picture {

    @NotEmpty(message = "Picture name is required")
    private final String name;

    @ValidFilename
    private final String filename;

    @Min(value = 1, message = "Picture size in bytes can't be zero")
    private final long sizeInBytes;

    @Valid
    private final PictureRepresentation pictureRepresentation;

    @JsonCreator
    public Picture(
            @JsonProperty("name") String name,
            @JsonProperty("filename") String filename,
            @JsonProperty("sizeInBytes") long sizeInBytes,
            @JsonProperty("mediaLink") PictureRepresentation pictureRepresentation
    ) {
        this.name = name;
        this.filename = filename;
        this.sizeInBytes = sizeInBytes;
        this.pictureRepresentation = pictureRepresentation;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
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

        if (sizeInBytes != picture.sizeInBytes) return false;
        if (name != null ? !name.equals(picture.name) : picture.name != null) return false;
        if (filename != null ? !filename.equals(picture.filename) : picture.filename != null) return false;
        return pictureRepresentation != null ? pictureRepresentation.equals(picture.pictureRepresentation) : picture.pictureRepresentation == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (int) (sizeInBytes ^ (sizeInBytes >>> 32));
        result = 31 * result + (pictureRepresentation != null ? pictureRepresentation.hashCode() : 0);
        return result;
    }
}
