package ec.animal.adoption.models.jpa;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "large_image")
public class JpaLargeImage extends JpaImage {

    @NotNull
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID animalUuid;

    private Timestamp creationDate;

    @NotNull
    private String imageType;

    @NotNull
    private String name;

    @NotNull
    private String url;

    public JpaLargeImage() {
        // Required by jpa
    }

    public JpaLargeImage(UUID animalUuid, String imageType, String name, String url) {
        this();
        this.id = UUID.randomUUID();
        this.animalUuid = animalUuid;
        this.creationDate = Timestamp.valueOf(LocalDateTime.now());
        this.imageType = imageType;
        this.name = name;
        this.url = url;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAnimalUuid() {
        return animalUuid;
    }

    public String getImageType() {
        return imageType;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaLargeImage that = (JpaLargeImage) o;

        if (animalUuid != null ? !animalUuid.equals(that.animalUuid) : that.animalUuid != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (imageType != null ? !imageType.equals(that.imageType) : that.imageType != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (imageType != null ? imageType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
