package ec.animal.adoption.domain.state;

import java.io.Serializable;
import java.util.Objects;

public class Unavailable extends State implements Serializable {
    private String notes;

    public Unavailable() {
        // Required for serialization
    }

    public Unavailable(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unavailable that = (Unavailable) o;
        return Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notes);
    }
}
