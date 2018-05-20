package ec.animal.adoption.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.state.State;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Animal {

    @NotEmpty(message = "Animal uuid is required")
    @JsonProperty("uuid")
    private final String uuid;

    @NotEmpty(message = "Animal name is required")
    @JsonProperty("name")
    private final String name;

    @NotNull(message = "Animal registration date is required")
    @JsonProperty("registrationDate")
    private final LocalDateTime registrationDate;

    @NotNull(message = "Animal type is required")
    @JsonProperty("type")
    private final Type type;

    @NotNull(message = "Animal estimated age is required")
    @JsonProperty("estimatedAge")
    private final EstimatedAge estimatedAge;

    @NotNull(message = "Animal state is required")
    @JsonProperty("state")
    private final State state;

    @JsonCreator
    public Animal(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("name") String name,
            @JsonProperty("registrationDate") LocalDateTime registrationDate,
            @JsonProperty("type") Type type,
            @JsonProperty("estimatedAge") EstimatedAge estimatedAge,
            @JsonProperty("state") State state
    ) {
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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public Type getType() {
        return type;
    }

    public EstimatedAge getEstimatedAge() {
        return estimatedAge;
    }

    public State getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        if (uuid != null ? !uuid.equals(animal.uuid) : animal.uuid != null) return false;
        if (name != null ? !name.equals(animal.name) : animal.name != null) return false;
        if (registrationDate != null ? !registrationDate.equals(animal.registrationDate) : animal.registrationDate != null)
            return false;
        if (type != animal.type) return false;
        if (estimatedAge != animal.estimatedAge) return false;
        return state != null ? state.equals(animal.state) : animal.state == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (estimatedAge != null ? estimatedAge.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
