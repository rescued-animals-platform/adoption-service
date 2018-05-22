package ec.animal.adoption;

import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;

import java.util.Random;

public class IntegrationTestUtils {

    private static final Type[] TYPES = Type.values();
    private static final EstimatedAge[] ESTIMATED_AGES = EstimatedAge.values();
    private static final Sex[] SEXES = Sex.values();
    private static final Size[] SIZES = Size.values();
    private static final PhysicalActivity[] PHYSICAL_ACTIVITIES = PhysicalActivity.values();

    public static Type getRandomType() {
        return TYPES[getRandomIndex(TYPES.length)];
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

    private static int getRandomIndex(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }
}