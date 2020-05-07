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

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.LinkPictureBuilder;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationBuilder;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.story.Story;

import java.time.LocalDateTime;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomEstimatedAge;
import static ec.animal.adoption.TestUtils.getRandomSex;
import static ec.animal.adoption.TestUtils.getRandomSpecies;
import static ec.animal.adoption.TestUtils.getRandomState;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class AnimalBuilder {

    private UUID animalId;
    private LocalDateTime registrationDate;
    private String clinicalRecord;
    private String name;
    private Species species;
    private EstimatedAge estimatedAge;
    private Sex sex;
    private State state;
    private Organization organization;
    private LinkPicture primaryLinkPicture;
    private Characteristics characteristics;
    private Story story;

    public static AnimalBuilder random() {
        AnimalBuilder animalBuilder = new AnimalBuilder();
        animalBuilder.animalId = UUID.randomUUID();
        animalBuilder.registrationDate = LocalDateTime.now();
        animalBuilder.clinicalRecord = randomAlphabetic(10);
        animalBuilder.name = randomAlphabetic(10);
        animalBuilder.species = getRandomSpecies();
        animalBuilder.estimatedAge = getRandomEstimatedAge();
        animalBuilder.sex = getRandomSex();
        animalBuilder.state = getRandomState();
        animalBuilder.organization = OrganizationBuilder.random().build();
        return animalBuilder;
    }

    public static AnimalBuilder randomWithDefaultOrganization() {
        AnimalBuilder animalBuilder = random();
        animalBuilder.organization = OrganizationBuilder.randomDefaultOrganization().build();
        return animalBuilder;
    }

    public static AnimalBuilder randomWithAnotherOrganization() {
        AnimalBuilder animalBuilder = random();
        animalBuilder.organization = OrganizationBuilder.randomAnotherOrganization().build();
        return animalBuilder;
    }

    public static AnimalBuilder randomWithPrimaryLinkPicture() {
        AnimalBuilder animalBuilder = random();
        animalBuilder.primaryLinkPicture = LinkPictureBuilder.random().withPictureType(PictureType.PRIMARY).build();
        return animalBuilder;
    }

    public AnimalBuilder withIdentifier(final UUID animalId) {
        this.animalId = animalId;
        return this;
    }

    public AnimalBuilder withRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public AnimalBuilder withClinicalRecord(final String clinicalRecord) {
        this.clinicalRecord = clinicalRecord;
        return this;
    }

    public AnimalBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public AnimalBuilder withSpecies(final Species species) {
        this.species = species;
        return this;
    }

    public AnimalBuilder withEstimatedAge(final EstimatedAge estimatedAge) {
        this.estimatedAge = estimatedAge;
        return this;
    }

    public AnimalBuilder withSex(final Sex sex) {
        this.sex = sex;
        return this;
    }

    public AnimalBuilder withState(final State state) {
        this.state = state;
        return this;
    }

    public AnimalBuilder withOrganization(final Organization organization) {
        this.organization = organization;
        return this;
    }

    public AnimalBuilder withPrimaryLinkPicture(final LinkPicture primaryLinkPicture) {
        this.primaryLinkPicture = primaryLinkPicture;
        return this;
    }

    public AnimalBuilder withCharacteristics(final Characteristics characteristics) {
        this.characteristics = characteristics;
        return this;
    }

    public AnimalBuilder withStory(final Story story) {
        this.story = story;
        return this;
    }

    public Animal build() {
        return new Animal(this.animalId,
                          this.registrationDate,
                          this.clinicalRecord,
                          this.name,
                          this.species,
                          this.estimatedAge,
                          this.sex,
                          this.state,
                          this.primaryLinkPicture,
                          this.characteristics,
                          this.story,
                          this.organization);
    }
}
