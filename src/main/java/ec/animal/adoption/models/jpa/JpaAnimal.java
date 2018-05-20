package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.state.State;

import javax.persistence.*;
import java.sql.Timestamp;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "state_id")
    private JpaState jpaState;

    private JpaAnimal() {
        // Required by jpa
    }

    public JpaAnimal(Animal animal) {
        this();
        this.uuid = animal.getUuid();
        this.name = animal.getName();
        this.registrationDate = Timestamp.valueOf(animal.getRegistrationDate());
        this.type = animal.getType().name();
        this.estimatedAge = animal.getEstimatedAge().name();
        this.jpaState = new JpaState(animal.getState());
    }

    public Animal toAnimal() {
        return new Animal(
                uuid,
                name,
                registrationDate.toLocalDateTime(),
                Type.valueOf(type),
                EstimatedAge.valueOf(estimatedAge),
                jpaState.toState()
        );
    }

    public void setState(State state) {
        this.jpaState.setState(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaAnimal jpaAnimal = (JpaAnimal) o;

        if (id != null ? !id.equals(jpaAnimal.id) : jpaAnimal.id != null) return false;
        if (uuid != null ? !uuid.equals(jpaAnimal.uuid) : jpaAnimal.uuid != null) return false;
        if (name != null ? !name.equals(jpaAnimal.name) : jpaAnimal.name != null) return false;
        if (type != null ? !type.equals(jpaAnimal.type) : jpaAnimal.type != null) return false;
        if (estimatedAge != null ? !estimatedAge.equals(jpaAnimal.estimatedAge) : jpaAnimal.estimatedAge != null)
            return false;
        if (registrationDate != null ? !registrationDate.equals(jpaAnimal.registrationDate) : jpaAnimal.registrationDate != null)
            return false;
        return jpaState != null ? jpaState.equals(jpaAnimal.jpaState) : jpaAnimal.jpaState == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (estimatedAge != null ? estimatedAge.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (jpaState != null ? jpaState.hashCode() : 0);
        return result;
    }
}
