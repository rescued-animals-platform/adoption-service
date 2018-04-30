package ec.animal.adoption.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class AnimalForAdoption {
    private final String uuid;
    private final String name;
    private final LocalDateTime registrationDate;

    public AnimalForAdoption(String uuid, String name, LocalDateTime registrationDate) {
        this.uuid = uuid;
        this.name = name;
        this.registrationDate = registrationDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalForAdoption that = (AnimalForAdoption) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(registrationDate, that.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, registrationDate);
    }
}
