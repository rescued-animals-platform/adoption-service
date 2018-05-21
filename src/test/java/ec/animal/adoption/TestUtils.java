package ec.animal.adoption;

import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class TestUtils {

    private static final List<State> states = Arrays.asList(
            new LookingForHuman(LocalDateTime.now()),
            new Adopted(LocalDate.now(), randomAlphabetic(10)),
            new Unavailable(randomAlphabetic(10))
    );
    private static final List<Type> types = Arrays.asList(Type.values());
    private static final List<EstimatedAge> estimatedAges = Arrays.asList(EstimatedAge.values());
    private static final List<Sex> sexes = Arrays.asList(Sex.values());

    public static State getRandomState() {
        return states.get(getRandomIndex(states));
    }

    public static Type getRandomType() {
        return types.get(getRandomIndex(types));
    }

    public static EstimatedAge getRandomEstimatedAge() {
        return estimatedAges.get(getRandomIndex(estimatedAges));
    }

    public static Sex getRandomSex() {
        return sexes.get(getRandomIndex(sexes));
    }

    private static int getRandomIndex(List items) {
        Random random = new Random();
        return random.nextInt(items.size());
    }
}