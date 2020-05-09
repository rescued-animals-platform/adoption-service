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

package ec.animal.adoption.domain.animal;

import ec.animal.adoption.domain.Entity;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.story.Story;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings({"PMD.DataClass", "PMD.NullAssignment", "PMD.ExcessiveParameterList"})
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

    public boolean has(final Story story) {
        return this.story != null && this.story.equals(story);
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
    @SuppressWarnings("PMD")
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

        if (!clinicalRecord.equals(animal.clinicalRecord)) {
            return false;
        }
        if (!name.equals(animal.name)) {
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
        if (!state.equals(animal.state)) {
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
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + clinicalRecord.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + species.hashCode();
        result = 31 * result + estimatedAge.hashCode();
        result = 31 * result + sex.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + (primaryLinkPicture != null ? primaryLinkPicture.hashCode() : 0);
        result = 31 * result + (characteristics != null ? characteristics.hashCode() : 0);
        result = 31 * result + (story != null ? story.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        return result;
    }

    public static class AnimalBuilder {

        private UUID animalId;
        private LocalDateTime registrationDate;
        private String clinicalRecord;
        private String name;
        private Species species;
        private EstimatedAge estimatedAge;
        private Sex sex;
        private State state;
        private LinkPicture primaryLinkPicture;
        private Characteristics characteristics;
        private Story story;
        private Organization organization;

        public static AnimalBuilder copyOf(final Animal animal) {
            AnimalBuilder animalBuilder = new AnimalBuilder();
            animalBuilder.animalId = animal.getIdentifier();
            animalBuilder.registrationDate = animal.getRegistrationDate();
            animalBuilder.clinicalRecord = animal.clinicalRecord;
            animalBuilder.name = animal.name;
            animalBuilder.species = animal.species;
            animalBuilder.estimatedAge = animal.estimatedAge;
            animalBuilder.sex = animal.sex;
            animalBuilder.state = animal.state;
            animalBuilder.primaryLinkPicture = animal.primaryLinkPicture;
            animalBuilder.characteristics = animal.characteristics;
            animalBuilder.story = animal.story;
            animalBuilder.organization = animal.organization;

            return animalBuilder;
        }

        public AnimalBuilder with(final Organization organization) {
            this.organization = organization;
            return this;
        }

        public AnimalBuilder with(final LinkPicture primaryLinkPicture) {
            this.primaryLinkPicture = primaryLinkPicture;
            return this;
        }

        public AnimalBuilder with(final Characteristics characteristics) {
            this.characteristics = characteristics;
            return this;
        }

        public AnimalBuilder with(final Story story) {
            if (this.story == null) {
                this.story = story;
            } else {
                this.story = this.story.updateWith(story);
            }

            return this;
        }

        public Animal build() {
            return new Animal(animalId,
                              registrationDate,
                              clinicalRecord,
                              name,
                              species,
                              estimatedAge,
                              sex,
                              state,
                              primaryLinkPicture,
                              characteristics,
                              story,
                              organization);
        }
    }
}
