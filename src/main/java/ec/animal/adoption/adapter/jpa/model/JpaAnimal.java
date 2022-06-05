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

package ec.animal.adoption.adapter.jpa.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "animal")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JpaAnimal implements Serializable {

    @Serial
    private static final transient long serialVersionUID = -632732651164438810L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @EqualsAndHashCode.Include
    private UUID id;

    private LocalDateTime registrationDate;

    @NotNull
    private String clinicalRecord;

    private String name;

    @NotNull
    private String species;

    @NotNull
    private String estimatedAge;

    @NotNull
    private String sex;

    @NotNull
    private String stateName;

    private String adoptionFormId;

    private String unavailableStateNotes;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "jpaAnimal")
    private JpaPrimaryLinkPicture jpaPrimaryLinkPicture;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "jpaAnimal")
    private JpaCharacteristics jpaCharacteristics;

    @Setter
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "jpaAnimal")
    private JpaStory jpaStory;

    @OneToOne
    @JoinColumn(name = "organization_id", updatable = false)
    private JpaOrganization jpaOrganization;
}
