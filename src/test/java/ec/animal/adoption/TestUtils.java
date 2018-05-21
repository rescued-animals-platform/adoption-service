package ec.animal.adoption;

import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.characteristics.temperament.Balance;
import ec.animal.adoption.domain.characteristics.temperament.Docility;
import ec.animal.adoption.domain.characteristics.temperament.Sociability;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class TestUtils {

    private static final List<State> states = Arrays.asList(
            new LookingForHuman(LocalDateTime.now()),
            new Adopted(LocalDate.now(), randomAlphabetic(10)),
            new Unavailable(randomAlphabetic(10))
    );
    private static final Type[] types = Type.values();
    private static final EstimatedAge[] estimatedAges = EstimatedAge.values();
    private static final Sex[] sexes = Sex.values();
    private static final Size[] sizes = Size.values();
    private static final PhysicalActivity[] physicalActivities = PhysicalActivity.values();
    private static final FriendlyWith[] friendlyWith = FriendlyWith.values();
    private static final Sociability[] sociability = Sociability.values();
    private static final Docility[] docility = Docility.values();
    private static final Balance[] balance = Balance.values();

    public static State getRandomState() {
        return states.get(getRandomIndex(states.size()));
    }

    public static Type getRandomType() {
        return types[getRandomIndex(types.length)];
    }

    public static EstimatedAge getRandomEstimatedAge() {
        return estimatedAges[getRandomIndex(estimatedAges.length)];
    }

    public static Sex getRandomSex() {
        return sexes[getRandomIndex(sexes.length)];
    }

    public static Size getRandomSize() {
        return sizes[getRandomIndex(sizes.length)];
    }

    public static PhysicalActivity getRandomPhysicalActivity() {
        return physicalActivities[getRandomIndex(physicalActivities.length)];
    }

    public static Temperament getRandomTemperament() {
        List<Temperament> temperaments = new ArrayList<>();
        Collections.addAll(temperaments, sociability);
        Collections.addAll(temperaments, docility);
        Collections.addAll(temperaments, balance);
        return temperaments.get(getRandomIndex(temperaments.size()));
    }

    public static Sociability getRandomSociability() {
        return sociability[getRandomIndex(sociability.length)];
    }

    public static Docility getRandomDocility() {
        return docility[getRandomIndex(docility.length)];
    }

    public static Balance getRandomBalance() {
        return balance[getRandomIndex(balance.length)];
    }

    public static FriendlyWith getRandomFriendlyWith() {
        return friendlyWith[getRandomIndex(friendlyWith.length)];
    }

    private static int getRandomIndex(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }
}