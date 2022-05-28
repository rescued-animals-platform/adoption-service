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

import ec.animal.adoption.adapter.jpa.service.JpaCharacteristicsMapper;
import ec.animal.adoption.adapter.jpa.service.JpaOrganizationMapper;
import ec.animal.adoption.adapter.jpa.service.JpaStoryMapper;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.EstimatedAge;
import ec.animal.adoption.domain.model.animal.Sex;
import ec.animal.adoption.domain.model.animal.Species;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.state.State;
import ec.animal.adoption.domain.model.state.StateName;
import ec.animal.adoption.domain.model.story.Story;
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
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Entity(name = "animal")
public class JpaAnimal implements Serializable {

    @Serial
    private static final transient long serialVersionUID = -632732651164438810L;

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
        this.jpaOrganization = JpaOrganizationMapper.MAPPER.toJpaOrganization(animal.getOrganization());
    }

    public JpaAnimal(final AnimalDto animalDto) {
        this.id = UUID.randomUUID();
        this.registrationDate = LocalDateTime.now();
        this.clinicalRecord = animalDto.clinicalRecord();
        this.name = animalDto.name();
        this.species = animalDto.species().name();
        this.estimatedAge = animalDto.estimatedAge().name();
        this.sex = animalDto.sex().name();
        this.stateName = animalDto.getStateNameAsString();
        this.adoptionFormId = animalDto.getAdoptionFormId().orElse(null);
        this.unavailableStateNotes = animalDto.getNotes().orElse(null);
        this.jpaOrganization = JpaOrganizationMapper.MAPPER.toJpaOrganization(animalDto.organization());
    }

    private void setJpaPrimaryLinkPicture(final LinkPicture primaryLinkPicture) {
        this.jpaPrimaryLinkPicture = primaryLinkPicture == null ? null :
                                     new JpaPrimaryLinkPicture(primaryLinkPicture, this);
    }

    private void setJpaCharacteristics(final Characteristics characteristics) {
        this.jpaCharacteristics = Optional.ofNullable(characteristics)
                                          .map(c -> JpaCharacteristicsMapper.MAPPER.toJpaCharacteristics(c, this))
                                          .orElse(null);
    }

    private void setJpaStory(final Story story) {
        this.jpaStory = Optional.ofNullable(story)
                                .map(s -> JpaStoryMapper.MAPPER.toJpaStory(s, this))
                                .orElse(null);
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
                JpaCharacteristicsMapper.MAPPER.toCharacteristics(this.jpaCharacteristics),
                JpaStoryMapper.MAPPER.toStory(this.jpaStory),
                JpaOrganizationMapper.MAPPER.toOrganization(this.jpaOrganization)
        );
    }

    @Override
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
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
