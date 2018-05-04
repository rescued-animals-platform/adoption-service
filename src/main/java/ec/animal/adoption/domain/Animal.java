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

    public Animal() {
        // Required for serialization
    }

    public Animal(String uuid, String name, LocalDateTime registrationDate, Type type) {
        this();
        this.uuid = uuid;
        this.name = name;
        this.registrationDate = registrationDate;
        this.type = type;
        this.state = new LookingForHuman(registrationDate);
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type.name();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal that = (Animal) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(registrationDate, that.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, type, registrationDate);
    }
}
