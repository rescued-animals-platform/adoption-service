package ec.animal.adoption;

import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestUtils {

    public static State getRandomState() {
        Random random = new Random();
        List<State> states = Arrays.asList(
                new LookingForHuman(LocalDateTime.now()),
                new Adopted(LocalDate.now(), RandomStringUtils.randomAlphabetic(10)),
                new Unavailable(RandomStringUtils.randomAlphabetic(10))
        );
        int randomStateIndex = random.nextInt(states.size());
        return states.get(randomStateIndex);
    }
}