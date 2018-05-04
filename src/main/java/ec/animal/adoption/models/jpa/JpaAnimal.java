package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "animal")
public class JpaAnimal {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String estimatedAge;

    private Timestamp registrationDate;

    @SuppressWarnings(value = "unused")
    public JpaAnimal() {
        // required by jpa
    }

    public JpaAnimal(Animal animal) {
        this.uuid = animal.getUuid();
        this.name = animal.getName();
        this.registrationDate = Timestamp.valueOf(animal.getRegistrationDate());
        this.type = animal.getType().name();
        this.estimatedAge = animal.getEstimatedAge().name();
    }

    public Animal toAnimal() {
        return new Animal(uuid, name, registrationDate.toLocalDateTime(), Type.valueOf(type), EstimatedAge.valueOf(estimatedAge));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaAnimal jpaAnimal = (JpaAnimal) o;
        return Objects.equals(id, jpaAnimal.id) &&
                Objects.equals(uuid, jpaAnimal.uuid) &&
                Objects.equals(name, jpaAnimal.name) &&
                Objects.equals(type, jpaAnimal.type) &&
                Objects.equals(estimatedAge, jpaAnimal.estimatedAge) &&
                Objects.equals(registrationDate, jpaAnimal.registrationDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, uuid, name, type, estimatedAge, registrationDate);
    }
}
