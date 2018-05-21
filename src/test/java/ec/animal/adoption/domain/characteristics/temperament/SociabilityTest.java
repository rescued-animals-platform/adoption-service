package ec.animal.adoption.domain.characteristics.temperament;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SociabilityTest {

    @Test
    public void shouldBeAnInstanceOfTemperament() {
        Sociability sociability = getRandomSociability();

        assertThat(sociability, is(instanceOf(Temperament.class)));
    }

    private static Sociability getRandomSociability() {
        Random random = new Random();
        List<Sociability> sociabilityScales = Arrays.asList(Sociability.values());
        int randomSociabilityIndex = random.nextInt(sociabilityScales.size());
        return sociabilityScales.get(randomSociabilityIndex);
    }
}