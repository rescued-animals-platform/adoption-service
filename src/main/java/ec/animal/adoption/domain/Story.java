package ec.animal.adoption.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public class Story {

    @JsonIgnore
    private UUID animalUuid;

    @NotEmpty(message = "Story text is required")
    @JsonProperty("text")
    private final String text;

    @JsonCreator
    public Story(@JsonProperty("text") String text) {
        this.text = text;
    }

    public void setAnimalUuid(UUID animalUuid) {
        this.animalUuid = animalUuid;
    }

    public UUID getAnimalUuid() {
        return this.animalUuid;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        if (animalUuid != null ? !animalUuid.equals(story.animalUuid) : story.animalUuid != null) return false;
        return text != null ? text.equals(story.text) : story.text == null;
    }

    @Override
    public int hashCode() {
        int result = animalUuid != null ? animalUuid.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
