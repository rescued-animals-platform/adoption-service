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

import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.EstimatedAge;
import ec.animal.adoption.domain.animal.Sex;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.animal.dto.CreateAnimalDto;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.StateName;
import ec.animal.adoption.domain.story.Story;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Entity(name = "animal")
@SuppressWarnings("PMD.ShortVariable")
public class JpaAnimal implements Serializable {

    private transient static final long serialVersionUID = -632732651164438810L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
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
    @SuppressWarnings({"PMD.SingularField", "PMD.UnusedPrivateField"})
    private String stateName;

    private String adoptionFormId;

    private String unavailableStateNotes;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "jpaAnimal")
    private JpaPrimaryLinkPicture jpaPrimaryLinkPicture;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "jpaAnimal")
    private JpaCharacteristics jpaCharacteristics;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "jpaAnimal")
    private JpaStory jpaStory;

    @OneToOne
    @JoinColumn(name = "organization_id", updatable = false)
    private JpaOrganization jpaOrganization;

    private JpaAnimal() {
        // Required by jpa
    }

    public JpaAnimal(final Animal animal) {
        this.id = animal.getIdentifier();
        this.registrationDate = animal.getRegistrationDate();
        this.clinicalRecord = animal.getClinicalRecord();
        this.name = animal.getName();
        this.species = animal.getSpecies().name();
        this.estimatedAge = animal.getEstimatedAge().name();
        this.sex = animal.getSex().name();
        this.stateName = animal.getStateName();
        this.adoptionFormId = animal.getState().getAdoptionFormId().orElse(null);
        this.unavailableStateNotes = animal.getState().getNotes().orElse(null);
        this.setJpaPrimaryLinkPicture(animal.getPrimaryLinkPicture().orElse(null));
        this.setJpaCharacteristics(animal.getCharacteristics().orElse(null));
        this.setJpaStory(animal.getStory().orElse(null));
        this.jpaOrganization = new JpaOrganization(animal.getOrganization());
    }

    public JpaAnimal(final CreateAnimalDto createAnimalDto) {
        this.id = UUID.randomUUID();
        this.registrationDate = LocalDateTime.now();
        this.clinicalRecord = createAnimalDto.getClinicalRecord();
        this.name = createAnimalDto.getName();
        this.species = createAnimalDto.getSpecies().name();
        this.estimatedAge = createAnimalDto.getEstimatedAge().name();
        this.sex = createAnimalDto.getSex().name();
        this.stateName = createAnimalDto.getStateNameAsString();
        this.adoptionFormId = createAnimalDto.getAdoptionFormId().orElse(null);
        this.unavailableStateNotes = createAnimalDto.getNotes().orElse(null);
        this.jpaOrganization = new JpaOrganization(createAnimalDto.getOrganization());
    }

    private void setJpaPrimaryLinkPicture(final LinkPicture primaryLinkPicture) {
        this.jpaPrimaryLinkPicture = primaryLinkPicture == null ? null :
                new JpaPrimaryLinkPicture(primaryLinkPicture, this);
    }

    private void setJpaCharacteristics(final Characteristics characteristics) {
        this.jpaCharacteristics = characteristics == null ? null :
                new JpaCharacteristics(characteristics, this);
    }

    private void setJpaStory(final Story story) {
        this.jpaStory = story == null ? null :
                new JpaStory(story, this);
    }

    public Animal toAnimal() {
        return new Animal(
                id,
                registrationDate,
                clinicalRecord,
                name,
                Species.valueOf(species),
                EstimatedAge.valueOf(estimatedAge),
                Sex.valueOf(sex),
                State.from(StateName.valueOf(this.stateName), this.adoptionFormId, this.unavailableStateNotes),
                ofNullable(jpaPrimaryLinkPicture).map(JpaPrimaryLinkPicture::toLinkPicture).orElse(null),
                ofNullable(jpaCharacteristics).map(JpaCharacteristics::toCharacteristics).orElse(null),
                ofNullable(jpaStory).map(JpaStory::toStory).orElse(null),
                this.jpaOrganization.toOrganization()
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

        JpaAnimal jpaAnimal = (JpaAnimal) o;

        return id != null ? id.equals(jpaAnimal.id) : jpaAnimal.id == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
