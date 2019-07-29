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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("PMD.ShortVariable")
    private Long id;

    @NotNull
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID animalUuid;

    private LocalDateTime creationDate;

    @NotNull
    private String size;

    @NotNull
    private String physicalActivity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "temperaments_id")
    private JpaTemperaments jpaTemperaments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jpaFriendlyWithId.jpaCharacteristics")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<JpaFriendlyWith> friendlyWith;

    private JpaCharacteristics() {
        // Required by jpa
    }

    public JpaCharacteristics(final Characteristics characteristics) {
        this();
        this.animalUuid = characteristics.getAnimalUuid();
        this.creationDate = LocalDateTime.now();
        this.size = characteristics.getSize().name();
        this.physicalActivity = characteristics.getPhysicalActivity().name();
        this.jpaTemperaments = new JpaTemperaments(characteristics.getTemperaments());
        this.friendlyWith = characteristics.getFriendlyWith().stream()
                .map(friendlyWith -> new JpaFriendlyWith(friendlyWith, this))
                .collect(Collectors.toList());
    }

    public Characteristics toCharacteristics() {
        final Characteristics characteristics = new Characteristics(
                Size.valueOf(this.size),
                PhysicalActivity.valueOf(this.physicalActivity),
                this.jpaTemperaments.toTemperaments(),
                this.friendlyWith.stream().map(JpaFriendlyWith::toFriendlyWith).toArray(FriendlyWith[]::new)
        );
        characteristics.setAnimalUuid(animalUuid);
        return characteristics;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaCharacteristics that = (JpaCharacteristics) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (animalUuid != null ? !animalUuid.equals(that.animalUuid) : that.animalUuid != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (physicalActivity != null ? !physicalActivity.equals(that.physicalActivity) : that.physicalActivity != null)
            return false;
        if (jpaTemperaments != null ? !jpaTemperaments.equals(that.jpaTemperaments) : that.jpaTemperaments != null)
            return false;
        return friendlyWith != null ? friendlyWith.equals(that.friendlyWith) : that.friendlyWith == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (animalUuid != null ? animalUuid.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (physicalActivity != null ? physicalActivity.hashCode() : 0);
        result = 31 * result + (jpaTemperaments != null ? jpaTemperaments.hashCode() : 0);
        result = 31 * result + (friendlyWith != null ? friendlyWith.hashCode() : 0);
        return result;
    }
}
