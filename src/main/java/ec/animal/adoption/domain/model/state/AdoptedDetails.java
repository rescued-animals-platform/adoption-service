package ec.animal.adoption.domain.model.state;

import org.springframework.lang.Nullable;

import java.util.Optional;

public class AdoptedDetails {

    private final String adoptionFormId;

    public AdoptedDetails(@Nullable final String adoptionFormId) {
        this.adoptionFormId = adoptionFormId;
    }

    public Optional<String> getAdoptionFormId() {
        return Optional.ofNullable(adoptionFormId);
    }
}
