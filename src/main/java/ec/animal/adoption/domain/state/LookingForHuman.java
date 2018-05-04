package ec.animal.adoption.domain.state;

import java.time.LocalDateTime;
import java.util.Objects;

public class LookingForHuman extends State {

    private LocalDateTime registrationDate;

    public LookingForHuman() {
        // Required for serialization
    }

    public LookingForHuman(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LookingForHuman that = (LookingForHuman) o;
        return Objects.equals(registrationDate, that.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationDate);
    }
}
