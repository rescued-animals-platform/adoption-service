package ec.animal.adoption.domain.state;

import org.springframework.lang.NonNull;

public class UnavailableDetails {

    private final String notes;

    public UnavailableDetails(@NonNull final String notes) {
        this.notes = notes;
    }

    @NonNull
    public String getNotes() {
        return notes;
    }
}
