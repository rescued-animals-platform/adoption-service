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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "characteristics")
public class JpaCharacteristics implements Serializable {

    private transient static final long serialVersionUID = -132432659169428820L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("PMD.ShortVariable")
    private Long id;

    private LocalDateTime creationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_uuid", nullable = false)
    private JpaAnimal jpaAnimal;

    @NotNull
    private String size;

    @NotNull
    private String physicalActivity;

    private String sociability;

    private String docility;

    private String balance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jpaCharacteristics")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<JpaFriendlyWith> friendlyWith;

    private JpaCharacteristics() {
        // Required by jpa
    }

    public JpaCharacteristics(final Characteristics characteristics, final JpaAnimal jpaAnimal) {
        this();
        this.creationDate = LocalDateTime.now();
        this.size = characteristics.getSize().name();
        this.physicalActivity = characteristics.getPhysicalActivity().name();
        Temperaments temperaments = characteristics.getTemperaments();
        this.sociability = temperaments.getSociability() == null ? null : temperaments.getSociability().name();
        this.docility = temperaments.getDocility() == null ? null : temperaments.getDocility().name();
        this.balance = temperaments.getBalance() == null ? null : temperaments.getBalance().name();
        this.friendlyWith = characteristics.getFriendlyWith().stream()
                .map(friendlyWith -> new JpaFriendlyWith(friendlyWith, this))
                .collect(Collectors.toList());
        this.jpaAnimal = jpaAnimal;
    }

    public Characteristics toCharacteristics() {
        return new Characteristics(
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaCharacteristics that = (JpaCharacteristics) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (jpaAnimal != null ? !jpaAnimal.equals(that.jpaAnimal) : that.jpaAnimal != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (physicalActivity != null ? !physicalActivity.equals(that.physicalActivity) : that.physicalActivity != null)
            return false;
        if (sociability != null ? !sociability.equals(that.sociability) : that.sociability != null) return false;
        if (docility != null ? !docility.equals(that.docility) : that.docility != null) return false;
        if (balance != null ? !balance.equals(that.balance) : that.balance != null) return false;
        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (jpaAnimal != null ? jpaAnimal.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (physicalActivity != null ? physicalActivity.hashCode() : 0);
        result = 31 * result + (sociability != null ? sociability.hashCode() : 0);
        result = 31 * result + (docility != null ? docility.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}
