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

package ec.animal.adoption.domain.model.animal;

import ec.animal.adoption.domain.model.Entity;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.characteristics.Characteristics;
import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.state.State;
import ec.animal.adoption.domain.model.story.Story;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Animal extends Entity {

    private final String clinicalRecord;
    private final String name;
    private final Species species;
    private final EstimatedAge estimatedAge;
    private final Sex sex;
    private final State state;
    private final LinkPicture primaryLinkPicture;
    private final Characteristics characteristics;
    private final Story story;
    private final Organization organization;

    public Animal(@NonNull final UUID animalId,
                  @NonNull final LocalDateTime registrationDate,
                  final String clinicalRecord,
                  final String name,
                  final Species species,
                  final EstimatedAge estimatedAge,
                  final Sex sex,
                  final State state,
                  final LinkPicture primaryLinkPicture,
                  final Characteristics characteristics,
                  final Story story,
                  final Organization organization) {
        super(animalId, registrationDate);

        if (primaryLinkPicture != null && !primaryLinkPicture.isPrimary()) {
            throw new IllegalArgumentException("Picture type should be PRIMARY");
        }

        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
        this.state = state;
        this.primaryLinkPicture = primaryLinkPicture;
        this.characteristics = characteristics;
        this.story = story;
        this.organization = organization;
    }

    public boolean isSameAs(final Animal anotherAnimal) {
        return this.getIdentifier() == anotherAnimal.getIdentifier();
    }

    public boolean has(final Story story) {
        return this.story != null && this.story.equals(story);
    }

    public boolean has(final Characteristics characteristics) {
        return this.characteristics != null && this.characteristics.equals(characteristics);
    }

    public Animal updateWith(@NonNull final AnimalDto animalDto) {
        return new Animal(this.getIdentifier(),
                          this.getRegistrationDate(),
                          animalDto.getClinicalRecord(),
                          animalDto.getName(),
                          animalDto.getSpecies(),
                          animalDto.getEstimatedAge(),
                          animalDto.getSex(),
                          animalDto.getState(),
                          this.getPrimaryLinkPicture().orElse(null),
                          this.getCharacteristics().orElse(null),
                          this.getStory().orElse(null),
                          this.getOrganization()
        );
    }

    public String getClinicalRecord() {
        return clinicalRecord;
    }

    public String getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public EstimatedAge getEstimatedAge() {
        return estimatedAge;
    }

    public Sex getSex() {
        return sex;
    }

    public State getState() {
        return state;
    }

    public String getStateName() {
        return state.getName().name();
    }

    public Optional<LinkPicture> getPrimaryLinkPicture() {
        return Optional.ofNullable(primaryLinkPicture);
    }

    public Optional<Characteristics> getCharacteristics() {
        return Optional.ofNullable(characteristics);
    }

    public Optional<Story> getStory() {
        return Optional.ofNullable(story);
    }

    public Organization getOrganization() {
        return organization;
    }

    public UUID getOrganizationId() {
        return this.organization.getOrganizationId();
    }

    @Override
    @NonNull
    public UUID getIdentifier() {
        return super.identifier;
    }

    @Override
    @NonNull
    public LocalDateTime getRegistrationDate() {
        return super.registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Animal animal = (Animal) o;

        if (clinicalRecord != null ? !clinicalRecord.equals(animal.clinicalRecord) : animal.clinicalRecord != null) {
            return false;
        }
        if (name != null ? !name.equals(animal.name) : animal.name != null) {
            return false;
        }
        if (species != animal.species) {
            return false;
        }
        if (estimatedAge != animal.estimatedAge) {
            return false;
        }
        if (sex != animal.sex) {
            return false;
        }
        if (state != null ? !state.equals(animal.state) : animal.state != null) {
            return false;
        }
        if (primaryLinkPicture != null ? !primaryLinkPicture.equals(animal.primaryLinkPicture) : animal.primaryLinkPicture != null) {
            return false;
        }
        if (characteristics != null ? !characteristics.equals(animal.characteristics) : animal.characteristics != null) {
            return false;
        }
        if (story != null ? !story.equals(animal.story) : animal.story != null) {
            return false;
        }
        return organization != null ? organization.equals(animal.organization) : animal.organization == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (clinicalRecord != null ? clinicalRecord.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (estimatedAge != null ? estimatedAge.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (primaryLinkPicture != null ? primaryLinkPicture.hashCode() : 0);
        result = 31 * result + (characteristics != null ? characteristics.hashCode() : 0);
        result = 31 * result + (story != null ? story.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        return result;
    }
}
