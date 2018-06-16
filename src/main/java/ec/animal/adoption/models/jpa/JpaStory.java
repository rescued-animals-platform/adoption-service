package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Story;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "story")
public class JpaStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp creationDate;

    @NotNull
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID animalUuid;

    @NotNull
    private String text;

    private JpaStory() {
        // Required by jpa
    }

    public JpaStory(Story story) {
        this();
        this.creationDate = Timestamp.valueOf(LocalDateTime.now());
        this.animalUuid = story.getAnimalUuid();
        this.text = story.getText();

    }

    public Story toStory() {
        Story story = new Story(text);
        story.setAnimalUuid(animalUuid);
        return story;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaStory jpaStory = (JpaStory) o;

        if (id != null ? !id.equals(jpaStory.id) : jpaStory.id != null) return false;
        if (creationDate != null ? !creationDate.equals(jpaStory.creationDate) : jpaStory.creationDate != null)
            return false;
        if (animalUuid != null ? !animalUuid.equals(jpaStory.animalUuid) : jpaStory.animalUuid != null) return false;
        return text != null ? text.equals(jpaStory.text) : jpaStory.text == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (animalUuid != null ? animalUuid.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
