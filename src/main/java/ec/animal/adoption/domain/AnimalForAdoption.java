package ec.animal.adoption.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class AnimalForAdoption implements Serializable {

    @NotEmpty
    private String uuid;

    @NotEmpty
    private String name;

    @JsonSerialize
    @JsonDeserialize
    private Type type;

    private LocalDateTime registrationDate;

    public AnimalForAdoption() {
        // Required for serialization
    }

    public AnimalForAdoption(String uuid, String name, LocalDateTime registrationDate, Type type) {
        this.uuid = uuid;
        this.name = name;
        this.registrationDate = registrationDate;
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalForAdoption that = (AnimalForAdoption) o;
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
