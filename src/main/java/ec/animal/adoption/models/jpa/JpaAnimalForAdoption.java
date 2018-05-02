package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.AnimalForAdoption;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "animal_for_adoption")
public class JpaAnimalForAdoption {
    @Id
    private String uuid;

    private String name;

    private Timestamp registrationDate;

    @SuppressWarnings(value = "unused")
    public JpaAnimalForAdoption() {
        // required by jpa
    }

    public JpaAnimalForAdoption(AnimalForAdoption animalForAdoption) {
        this.uuid = animalForAdoption.getUuid();
        this.name = animalForAdoption.getName();
        this.registrationDate = Timestamp.valueOf(animalForAdoption.getRegistrationDate());
    }

    public AnimalForAdoption toAvailableAnimal() {
        return new AnimalForAdoption(uuid, name, registrationDate.toLocalDateTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaAnimalForAdoption that = (JpaAnimalForAdoption) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(registrationDate, that.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, registrationDate);
    }
}
