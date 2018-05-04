package ec.animal.adoption.domain;

import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Animal implements Serializable {

    @NotEmpty(message = "Animal uuid is required")
    private String uuid;

    @NotEmpty(message = "Animal name is required")
    private String name;

    @NotNull(message = "Animal type is required")
    private Type type;

    @NotNull(message = "Animal state is required")
    private State state;

    @NotNull(message = "Animal registration date is required")
    private LocalDateTime registrationDate;

    @NotNull(message = "Animal estimated age is required")
    private EstimatedAge estimatedAge;

    public Animal() {
        // Required for serialization
    }

    public Animal(String uuid, String name, LocalDateTime registrationDate, Type type, EstimatedAge estimatedAge) {
        this();
        this.uuid = uuid;
        this.name = name;
        this.registrationDate = registrationDate;
        this.type = type;
        this.estimatedAge = estimatedAge;
        this.state = new LookingForHuman(registrationDate);
    }

    public Animal(String uuid, String name, LocalDateTime registrationDate, Type type, EstimatedAge estimatedAge, State state) {
        this.uuid = uuid;
        this.name = name;
        this.registrationDate = registrationDate;
        this.type = type;
        this.estimatedAge = estimatedAge;
        this.state = state;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public EstimatedAge getEstimatedAge() {
        return estimatedAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(uuid, animal.uuid) &&
                Objects.equals(name, animal.name) &&
                type == animal.type &&
                Objects.equals(state, animal.state) &&
                Objects.equals(registrationDate, animal.registrationDate) &&
                estimatedAge == animal.estimatedAge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, type, state, registrationDate, estimatedAge);
    }
}
