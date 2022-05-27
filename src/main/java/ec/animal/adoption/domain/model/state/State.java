package ec.animal.adoption.domain.model.state;

import org.springframework.lang.NonNull;

import java.util.Optional;

import static ec.animal.adoption.domain.model.state.StateName.ADOPTED;
import static ec.animal.adoption.domain.model.state.StateName.LOOKING_FOR_HUMAN;
import static ec.animal.adoption.domain.model.state.StateName.UNAVAILABLE;

public final class State {

    private final StateName name;
    private AdoptedDetails adoptedDetails;
    private UnavailableDetails unavailableDetails;

    public static State from(@NonNull final StateName name,
                             final String adoptionFormId,
                             final String notes) {
        switch (name) {
            case LOOKING_FOR_HUMAN:
                return State.lookingForHuman();
            case ADOPTED:
                return State.adopted(adoptionFormId);
            case UNAVAILABLE:
                return State.unavailable(notes);
            default:
                throw new IllegalStateException();
        }
    }

    private State(@NonNull final StateName name) {
        this.name = name;
    }

    private State(@NonNull final StateName name, final AdoptedDetails adoptedDetails) {
        this.name = name;
        this.adoptedDetails = adoptedDetails;
    }

    private State(@NonNull final StateName name, final UnavailableDetails unavailableDetails) {
        this.name = name;
        this.unavailableDetails = unavailableDetails;
    }

    public static State lookingForHuman() {
        return new State(LOOKING_FOR_HUMAN);
    }

    public static State adopted(final String adoptionFormId) {
        return new State(ADOPTED, new AdoptedDetails(adoptionFormId));
    }

    public static State unavailable(@NonNull final String notes) {
        return new State(UNAVAILABLE, new UnavailableDetails(notes));
    }

    public StateName getName() {
        return name;
    }

    public Optional<String> getAdoptionFormId() {
        return Optional.ofNullable(adoptedDetails).flatMap(AdoptedDetails::getAdoptionFormId);
    }

    public Optional<String> getNotes() {
        return Optional.ofNullable(unavailableDetails).map(UnavailableDetails::getNotes);
    }
}
