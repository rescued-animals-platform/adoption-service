package ec.animal.adoption;

import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.AnimalSpecies;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperaments.Balance;
import ec.animal.adoption.domain.characteristics.temperaments.Docility;
import ec.animal.adoption.domain.characteristics.temperaments.Sociability;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.domain.media.SupportedImageExtension;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class TestUtils {

    private static final List<State> STATES = Arrays.asList(
            new LookingForHuman(LocalDateTime.now()),
            new Adopted(LocalDate.now(), randomAlphabetic(10)),
            new Unavailable(randomAlphabetic(10))
    );
    private static final AnimalSpecies[] ANIMAL_SPECIES = AnimalSpecies.values();
    private static final EstimatedAge[] ESTIMATED_AGES = EstimatedAge.values();
    private static final Sex[] SEXES = Sex.values();
    private static final Size[] SIZES = Size.values();
    private static final PhysicalActivity[] PHYSICAL_ACTIVITIES = PhysicalActivity.values();
    private static final FriendlyWith[] FRIENDLY_WITH = FriendlyWith.values();
    private static final Sociability[] SOCIABILITY = Sociability.values();
    private static final Docility[] DOCILITY = Docility.values();
    private static final Balance[] BALANCE = Balance.values();
    private static final SupportedImageExtension[] SUPPORTED_IMAGE_EXTENSIONS = SupportedImageExtension.values();
    private static final PictureType[] PICTURE_TYPES = PictureType.values();

    public static State getRandomState() {
        return STATES.get(getRandomIndex(STATES.size()));
    }

    public static AnimalSpecies getRandomAnimalSpecies() {
        return ANIMAL_SPECIES[getRandomIndex(ANIMAL_SPECIES.length)];
    }

    public static EstimatedAge getRandomEstimatedAge() {
        return ESTIMATED_AGES[getRandomIndex(ESTIMATED_AGES.length)];
    }

    public static Sex getRandomSex() {
        return SEXES[getRandomIndex(SEXES.length)];
    }

    public static Size getRandomSize() {
        return SIZES[getRandomIndex(SIZES.length)];
    }

    public static PhysicalActivity getRandomPhysicalActivity() {
        return PHYSICAL_ACTIVITIES[getRandomIndex(PHYSICAL_ACTIVITIES.length)];
    }

    public static FriendlyWith getRandomFriendlyWith() {
        return FRIENDLY_WITH[getRandomIndex(FRIENDLY_WITH.length)];
    }

    public static Sociability getRandomSociability() {
        return SOCIABILITY[getRandomIndex(SOCIABILITY.length)];
    }

    public static Docility getRandomDocility() {
        return DOCILITY[getRandomIndex(DOCILITY.length)];
    }

    public static Balance getRandomBalance() {
        return BALANCE[getRandomIndex(BALANCE.length)];
    }

    public static SupportedImageExtension getRandomSupportedImageExtension() {
        return SUPPORTED_IMAGE_EXTENSIONS[getRandomIndex(SUPPORTED_IMAGE_EXTENSIONS.length)];
    }

    public static PictureType getRandomPictureType() {
        return PICTURE_TYPES[getRandomIndex(PICTURE_TYPES.length)];
    }

    public static Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static int getRandomIndex(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }
}