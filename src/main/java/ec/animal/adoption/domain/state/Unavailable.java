package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Unavailable implements State {

    @JsonProperty("notes")
    private final String notes;

    @JsonCreator
    public Unavailable(@JsonProperty("notes") String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unavailable that = (Unavailable) o;

        return notes != null ? notes.equals(that.notes) : that.notes == null;
    }

    @Override
    public int hashCode() {
        return notes != null ? notes.hashCode() : 0;
    }
}
