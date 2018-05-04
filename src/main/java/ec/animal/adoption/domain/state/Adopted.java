package ec.animal.adoption.domain.state;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Adopted extends State implements Serializable {
    private LocalDate adoptionDate;
    private String adoptionFormId;

    public Adopted() {
        // Required for serialization
    }

    public Adopted(LocalDate adoptionDate, String adoptionFormId) {
        this.adoptionDate = adoptionDate;
        this.adoptionFormId = adoptionFormId;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public String getAdoptionFormId() {
        return adoptionFormId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopted adopted = (Adopted) o;
        return Objects.equals(adoptionDate, adopted.adoptionDate) &&
                Objects.equals(adoptionFormId, adopted.adoptionFormId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adoptionDate, adoptionFormId);
    }
}
