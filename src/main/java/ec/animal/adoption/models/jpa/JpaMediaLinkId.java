package ec.animal.adoption.models.jpa;

import org.hibernate.annotations.Type;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public class JpaMediaLinkId implements Serializable {

    @NotNull
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID animalUuid;

    @NotNull
    private String mediaName;

    private JpaMediaLinkId() {
        // Required by jpa
    }

    public JpaMediaLinkId(@NotNull UUID animalUuid, @NotNull String mediaName) {
        this();
        this.animalUuid = animalUuid;
        this.mediaName = mediaName;
    }

    public UUID getAnimalUuid() {
        return animalUuid;
    }

    public String getMediaName() {
        return mediaName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaMediaLinkId that = (JpaMediaLinkId) o;

        if (animalUuid != null ? !animalUuid.equals(that.animalUuid) : that.animalUuid != null) return false;
        return mediaName != null ? mediaName.equals(that.mediaName) : that.mediaName == null;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (mediaName != null ? mediaName.hashCode() : 0);
        return result;
    }
}
