package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.AnimalSpecies;
import ec.animal.adoption.domain.state.State;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "animal")
public class JpaAnimal {

    @Id
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @NotNull
    private String clinicalRecord;

    @NotNull
    private String name;

    @NotNull
    private String animalSpecies;

    @NotNull
    private String estimatedAge;

    @NotNull
    private String sex;

    private Timestamp registrationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "state_id")
    private JpaState jpaState;

    private JpaAnimal() {
        // Required by jpa
    }

    public JpaAnimal(Animal animal) {
        this();
        this.uuid = UUID.randomUUID();
        this.clinicalRecord = animal.getClinicalRecord();
        this.name = animal.getName();
        this.registrationDate = Timestamp.valueOf(animal.getRegistrationDate());
        this.animalSpecies = animal.getAnimalSpecies().name();
        this.estimatedAge = animal.getEstimatedAge().name();
        this.sex = animal.getSex().name();
        this.jpaState = new JpaState(animal.getState());
    }

    public Animal toAnimal() {
        return new Animal(
                clinicalRecord,
                name,
                registrationDate.toLocalDateTime(),
                AnimalSpecies.valueOf(animalSpecies),
                EstimatedAge.valueOf(estimatedAge),
                Sex.valueOf(sex),
                jpaState.toState()
        ).setUuid(uuid);
    }

    public void setState(State state) {
        this.jpaState.setState(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaAnimal jpaAnimal = (JpaAnimal) o;

        if (uuid != null ? !uuid.equals(jpaAnimal.uuid) : jpaAnimal.uuid != null) return false;
        if (clinicalRecord != null ? !clinicalRecord.equals(jpaAnimal.clinicalRecord) : jpaAnimal.clinicalRecord != null)
            return false;
        if (name != null ? !name.equals(jpaAnimal.name) : jpaAnimal.name != null) return false;
        if (animalSpecies != null ? !animalSpecies.equals(jpaAnimal.animalSpecies) : jpaAnimal.animalSpecies != null) return false;
        if (estimatedAge != null ? !estimatedAge.equals(jpaAnimal.estimatedAge) : jpaAnimal.estimatedAge != null)
            return false;
        if (sex != null ? !sex.equals(jpaAnimal.sex) : jpaAnimal.sex != null) return false;
        if (registrationDate != null ? !registrationDate.equals(jpaAnimal.registrationDate) : jpaAnimal.registrationDate != null)
            return false;
        return jpaState != null ? jpaState.equals(jpaAnimal.jpaState) : jpaAnimal.jpaState == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (clinicalRecord != null ? clinicalRecord.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (animalSpecies != null ? animalSpecies.hashCode() : 0);
        result = 31 * result + (estimatedAge != null ? estimatedAge.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (jpaState != null ? jpaState.hashCode() : 0);
        return result;
    }
}
