package ec.animal.adoption.domain.characteristics.temperament;

import ec.animal.adoption.TestUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SociabilityTest {

    @Test
    public void shouldBeAnInstanceOfTemperament() {
        Sociability sociability = TestUtils.getRandomSociability();

        assertThat(sociability, is(instanceOf(Temperament.class)));
    }
}