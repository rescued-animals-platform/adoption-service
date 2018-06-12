package ec.animal.adoption.models.jpa;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "small_image")
public class JpaSmallImage extends JpaImage {

    @NotNull
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID largeImageId;

    private Timestamp creationDate;

    @NotNull
    private String url;

    public JpaSmallImage() {
        // Required by jpa
    }

    public JpaSmallImage(UUID largeImageId, String url) {
        this();
        this.id = UUID.randomUUID();
        this.largeImageId = largeImageId;
        this.creationDate = Timestamp.valueOf(LocalDateTime.now());
        this.url = url;
    }

    public UUID getLargeImageId() {
        return this.largeImageId;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaSmallImage that = (JpaSmallImage) o;

        if (largeImageId != null ? !largeImageId.equals(that.largeImageId) : that.largeImageId != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = largeImageId != null ? largeImageId.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
