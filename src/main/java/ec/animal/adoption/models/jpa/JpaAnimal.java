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

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Species;
import ec.animal.adoption.domain.state.State;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity(name = "animal")
public class JpaAnimal implements Serializable {

    private transient static final long serialVersionUID = -632732651164438810L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
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

    @NotNull
    @SuppressWarnings({"PMD.SingularField", "PMD.UnusedPrivateField"})
    private String stateName;

    @NotNull
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private State state;

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
        this.stateName = animal.getStateName();
        this.state = animal.getState();
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
                state
        );
    }
}
