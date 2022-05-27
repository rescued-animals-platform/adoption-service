package ec.animal.adoption.domain.model.state;

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
