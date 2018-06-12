package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.media.MediaLink;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(name = "media_link")
public class JpaMediaLink {

    @EmbeddedId
    private JpaMediaLinkId jpaMediaLinkId;

    private Timestamp creationDate;

    @NotNull
    private String url;

    public JpaMediaLink() {
        // Required by jpa
    }

    public JpaMediaLink(MediaLink mediaLink) {
        this();
        this.jpaMediaLinkId = new JpaMediaLinkId(mediaLink.getAnimalUuid(), mediaLink.getMediaName());
        this.creationDate = Timestamp.valueOf(LocalDateTime.now());
        this.url = mediaLink.getUrl();
    }

    public MediaLink toMediaLink() {
        return new MediaLink(this.jpaMediaLinkId.getAnimalUuid(), this.jpaMediaLinkId.getMediaName(), this.url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaMediaLink that = (JpaMediaLink) o;

        if (jpaMediaLinkId != null ? !jpaMediaLinkId.equals(that.jpaMediaLinkId) : that.jpaMediaLinkId != null)
            return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = jpaMediaLinkId != null ? jpaMediaLinkId.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
