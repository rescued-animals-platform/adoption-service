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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.Entity;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.story.Story;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("PMD.DataClass")
public class Animal extends Entity {

    @NotEmpty(message = "Animal clinical record is required")
    @JsonProperty("clinicalRecord")
    private final String clinicalRecord;

    @NotEmpty(message = "Animal name is required")
    @JsonProperty("name")
    private final String name;

    @NotNull(message = "Animal species is required")
    @JsonProperty("species")
    private final Species species;

    @NotNull(message = "Animal estimated age is required")
    @JsonProperty("estimatedAge")
    private final EstimatedAge estimatedAge;

    @NotNull(message = "Animal sex is required")
    @JsonProperty("sex")
    private final Sex sex;

    @JsonProperty("state")
    private final State state;

    @JsonProperty(value = "primaryLinkPicture", access = JsonProperty.Access.READ_ONLY)
    private LinkPicture primaryLinkPicture;

    @JsonProperty(value = "characteristics", access = JsonProperty.Access.READ_ONLY)
    private Characteristics characteristics;

    @JsonProperty(value = "story", access = JsonProperty.Access.READ_ONLY)
    private Story story;

    @JsonIgnore
    private Organization organization;

    @JsonCreator
    private Animal(
            @JsonProperty("clinicalRecord") final String clinicalRecord,
            @JsonProperty("name") final String name,
            @JsonProperty("species") final Species species,
            @JsonProperty("estimatedAge") final EstimatedAge estimatedAge,
            @JsonProperty("sex") final Sex sex,
            @JsonProperty("state") final State state
    ) {
        super(null, null);
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
        this.state = Objects.requireNonNullElseGet(state, () -> new LookingForHuman(LocalDateTime.now()));
    }

    public Animal(
            final UUID uuid,
            final LocalDateTime registrationDate,
            final String clinicalRecord,
            final String name,
            final Species species,
            final EstimatedAge estimatedAge,
            final Sex sex,
            final State state
    ) {
        super(uuid, registrationDate);
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
        this.state = state;
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

    @JsonIgnore
    public String getStateName() {
        return state.getStateName();
    }

    public void setPrimaryLinkPicture(final LinkPicture primaryLinkPicture) {
        if (!PictureType.PRIMARY.equals(primaryLinkPicture.getPictureType())) {
            throw new IllegalArgumentException("Picture type should be PRIMARY");
        }

        this.primaryLinkPicture = primaryLinkPicture;
    }

    public Optional<LinkPicture> getPrimaryLinkPicture() {
        return Optional.ofNullable(primaryLinkPicture);
    }

    public void setCharacteristics(final Characteristics characteristics) {
        this.characteristics = characteristics;
    }

    public Optional<Characteristics> getCharacteristics() {
        return Optional.ofNullable(characteristics);
    }

    public void setStory(final Story story) {
        this.story = story;
    }

    public Optional<Story> getStory() {
        return Optional.ofNullable(story);
    }

    public void setOrganization(final Organization organization) {
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    @JsonIgnore
    public UUID getOrganizationUuid() {
        return this.organization.getOrganizationUuid();
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
}
