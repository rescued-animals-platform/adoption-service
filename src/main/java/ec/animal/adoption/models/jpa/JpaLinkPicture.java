package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.domain.media.PictureType;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "link_picture")
public class JpaLinkPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp creationDate;

    @NotNull
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID animalUuid;

    @NotNull
    private String name;

    @NotNull
    private String pictureType;

    @NotNull
    private String largeImageUrl;

    @NotNull
    private String smallImageUrl;

    private JpaLinkPicture() {
        // Required by jpa
    }

    public JpaLinkPicture(Picture picture) {
        this();
        if(!picture.hasUrls()) {
            throw new IllegalArgumentException();
        }
        this.creationDate = Timestamp.valueOf(LocalDateTime.now());
        this.animalUuid = picture.getAnimalUuid();
        this.name = picture.getName();
        this.pictureType = picture.getPictureType().name();
        this.largeImageUrl = picture.getLargeImageUrl();
        this.smallImageUrl = picture.getSmallImageUrl();
    }

    public Picture toPicture() {
        return new LinkPicture(
                this.animalUuid,
                this.name,
                PictureType.valueOf(this.pictureType),
                new MediaLink(this.largeImageUrl),
                new MediaLink(this.smallImageUrl)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaLinkPicture that = (JpaLinkPicture) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (animalUuid != null ? !animalUuid.equals(that.animalUuid) : that.animalUuid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pictureType != null ? !pictureType.equals(that.pictureType) : that.pictureType != null) return false;
        if (largeImageUrl != null ? !largeImageUrl.equals(that.largeImageUrl) : that.largeImageUrl != null)
            return false;
        return smallImageUrl != null ? smallImageUrl.equals(that.smallImageUrl) : that.smallImageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (animalUuid != null ? animalUuid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pictureType != null ? pictureType.hashCode() : 0);
        result = 31 * result + (largeImageUrl != null ? largeImageUrl.hashCode() : 0);
        result = 31 * result + (smallImageUrl != null ? smallImageUrl.hashCode() : 0);
        return result;
    }
}
