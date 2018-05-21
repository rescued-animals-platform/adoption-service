package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class LookingForHuman implements State {

    @JsonProperty("registrationDate")
    private final LocalDateTime registrationDate;

    @JsonCreator
    public LookingForHuman(@JsonProperty("registrationDate") LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LookingForHuman that = (LookingForHuman) o;

        return registrationDate != null ? registrationDate.equals(that.registrationDate) : that.registrationDate == null;
    }

    @Override
    public int hashCode() {
        return registrationDate != null ? registrationDate.hashCode() : 0;
    }
}
