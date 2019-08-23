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

package ec.animal.adoption.builders;

import ec.animal.adoption.domain.*;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.state.State;

import java.time.LocalDateTime;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@SuppressWarnings("PMD.TooManyMethods")
public class AnimalBuilder {

    private UUID uuid;
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

    public static AnimalBuilder random() {
        AnimalBuilder animalBuilder = new AnimalBuilder();
        animalBuilder.clinicalRecord = randomAlphabetic(10);
        animalBuilder.name = randomAlphabetic(10);
        animalBuilder.species = getRandomSpecies();
        animalBuilder.estimatedAge = getRandomEstimatedAge();
        animalBuilder.sex = getRandomSex();
        animalBuilder.state = getRandomState();
        return animalBuilder;
    }

    public AnimalBuilder withUuid(final UUID uuid) {
        this.uuid = uuid;
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
        Animal animal = new Animal(
                this.uuid,
                this.registrationDate,
                this.clinicalRecord,
                this.name,
                this.species,
                this.estimatedAge,
                this.sex,
                this.state
        );
        animal.setCharacteristics(characteristics);
        animal.setStory(story);

        if (primaryLinkPicture != null) {
            animal.setPrimaryLinkPicture(primaryLinkPicture);
        }

        return animal;
    }
}
