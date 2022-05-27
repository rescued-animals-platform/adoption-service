package ec.animal.adoption.domain.animal;

import ec.animal.adoption.domain.animal.characteristics.Characteristics;
import ec.animal.adoption.domain.animal.media.LinkPicture;
import ec.animal.adoption.domain.animal.story.Story;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.state.State;

import java.time.LocalDateTime;
import java.util.UUID;

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

    public static AnimalBuilder copyOf(final Animal animal) {
        AnimalBuilder animalBuilder = new AnimalBuilder();
        animalBuilder.animalId = animal.getIdentifier();
        animalBuilder.registrationDate = animal.getRegistrationDate();
        animalBuilder.clinicalRecord = animal.getClinicalRecord();
        animalBuilder.name = animal.getName();
        animalBuilder.species = animal.getSpecies();
        animalBuilder.estimatedAge = animal.getEstimatedAge();
        animalBuilder.sex = animal.getSex();
        animalBuilder.state = animal.getState();
        animalBuilder.organization = animal.getOrganization();
        animalBuilder.primaryLinkPicture = animal.getPrimaryLinkPicture().orElse(null);
        animalBuilder.characteristics = animal.getCharacteristics().orElse(null);
        animalBuilder.story = animal.getStory().orElse(null);

        return animalBuilder;
    }

    public AnimalBuilder with(final LinkPicture linkPicture) {
        this.primaryLinkPicture = this.primaryLinkPicture == null
                ? linkPicture
                : this.primaryLinkPicture.updateWith(linkPicture);

        return this;
    }

    public AnimalBuilder with(final Characteristics characteristics) {
        this.characteristics = this.characteristics == null
                ? characteristics
                : this.characteristics.updateWith(characteristics);

        return this;
    }

    public AnimalBuilder with(final Story story) {
        this.story = this.story == null ? story : this.story.updateWith(story);
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
