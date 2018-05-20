package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Adopted extends State {
    @JsonProperty("adoptionDate")
    private final LocalDate adoptionDate;

    @JsonProperty("adoptionFormId")
    private final String adoptionFormId;

    @JsonCreator
    public Adopted(
            @JsonProperty("adoptionDate") LocalDate adoptionDate,
            @JsonProperty("adoptionFormId") String adoptionFormId
    ) {
        this.adoptionDate = adoptionDate;
        this.adoptionFormId = adoptionFormId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adopted adopted = (Adopted) o;

        if (adoptionDate != null ? !adoptionDate.equals(adopted.adoptionDate) : adopted.adoptionDate != null)
            return false;
        return adoptionFormId != null ? adoptionFormId.equals(adopted.adoptionFormId) : adopted.adoptionFormId == null;
    }

    @Override
    public int hashCode() {
        int result = adoptionDate != null ? adoptionDate.hashCode() : 0;
        result = 31 * result + (adoptionFormId != null ? adoptionFormId.hashCode() : 0);
        return result;
    }
}
