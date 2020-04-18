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

package ec.animal.adoption.repository.jpa.model;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(name = "characteristics")
public class JpaCharacteristics implements Serializable {

    private transient static final long serialVersionUID = -132432659169428820L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_uuid")
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private JpaAnimal jpaAnimal;

    private LocalDateTime registrationDate;

    @NotNull
    private String size;

    @NotNull
    private String physicalActivity;

    private String sociability;

    private String docility;

    private String balance;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "characteristics_uuid", nullable = false)
    private List<JpaFriendlyWith> friendlyWith;

    private JpaCharacteristics() {
        // Required by jpa
    }

    public JpaCharacteristics(final Characteristics characteristics, final JpaAnimal jpaAnimal) {
        this();
        this.setUuid(characteristics.getUuid());
        this.jpaAnimal = jpaAnimal;
        this.setRegistrationDate(characteristics.getRegistrationDate());
        this.size = characteristics.getSize().name();
        this.physicalActivity = characteristics.getPhysicalActivity().name();
        this.setTemperaments(characteristics.getTemperaments());
        this.friendlyWith = characteristics.getFriendlyWith().stream().map(JpaFriendlyWith::new)
                                           .collect(Collectors.toList());
    }

    private void setUuid(final UUID uuid) {
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    }

    private void setRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate == null ? LocalDateTime.now() : registrationDate;
    }

    private void setTemperaments(final Temperaments temperaments) {
        this.sociability = temperaments.getSociability() == null ? null : temperaments.getSociability().name();
        this.docility = temperaments.getDocility() == null ? null : temperaments.getDocility().name();
        this.balance = temperaments.getBalance() == null ? null : temperaments.getBalance().name();
    }

    public Characteristics toCharacteristics() {
        return new Characteristics(
                this.uuid,
                this.registrationDate,
                Size.valueOf(this.size),
                PhysicalActivity.valueOf(this.physicalActivity),
                new Temperaments(
                        this.sociability == null ? null : Sociability.valueOf(sociability),
                        this.docility == null ? null : Docility.valueOf(this.docility),
                        this.balance == null ? null : Balance.valueOf(this.balance)
                ),
                this.friendlyWith.stream().map(JpaFriendlyWith::toFriendlyWith).toArray(FriendlyWith[]::new)
        );
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JpaCharacteristics that = (JpaCharacteristics) o;

        return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
