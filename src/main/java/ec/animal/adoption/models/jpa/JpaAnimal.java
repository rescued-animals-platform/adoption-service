/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Species;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "animal")
public class JpaAnimal implements Serializable {

    private transient static final long serialVersionUID = -632732651164438810L;

    @Id
    @org.hibernate.annotations.Type(type = "org.hibernate.type.PostgresUUIDType")
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

    private LocalDateTime registrationDate;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "jpaAnimal")
    private JpaState jpaState;

    private JpaAnimal() {
        // Required by jpa
    }

    public JpaAnimal(final Animal animal) {
        this();
        this.uuid = UUID.randomUUID();
        this.clinicalRecord = animal.getClinicalRecord();
        this.name = animal.getName();
        this.registrationDate = LocalDateTime.now();
        this.animalSpecies = animal.getSpecies().name();
        this.estimatedAge = animal.getEstimatedAge().name();
        this.sex = animal.getSex().name();
        this.jpaState = new JpaState(animal.getState(), this);
    }

    public Animal toAnimal() {
        return new Animal(
                uuid,
                registrationDate,
                clinicalRecord,
                name,
                Species.valueOf(animalSpecies),
                EstimatedAge.valueOf(estimatedAge),
                Sex.valueOf(sex),
                jpaState.toState()
        );
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaAnimal jpaAnimal = (JpaAnimal) o;

        if (uuid != null ? !uuid.equals(jpaAnimal.uuid) : jpaAnimal.uuid != null) return false;
        if (clinicalRecord != null ? !clinicalRecord.equals(jpaAnimal.clinicalRecord) : jpaAnimal.clinicalRecord != null)
            return false;
        if (name != null ? !name.equals(jpaAnimal.name) : jpaAnimal.name != null) return false;
        if (animalSpecies != null ? !animalSpecies.equals(jpaAnimal.animalSpecies) : jpaAnimal.animalSpecies != null)
            return false;
        if (estimatedAge != null ? !estimatedAge.equals(jpaAnimal.estimatedAge) : jpaAnimal.estimatedAge != null)
            return false;
        if (sex != null ? !sex.equals(jpaAnimal.sex) : jpaAnimal.sex != null) return false;
        if (registrationDate != null ? !registrationDate.equals(jpaAnimal.registrationDate) : jpaAnimal.registrationDate != null)
            return false;
        return jpaState != null ? jpaState.equals(jpaAnimal.jpaState) : jpaAnimal.jpaState == null;
    }

    @Override
    @SuppressWarnings("PMD")
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
