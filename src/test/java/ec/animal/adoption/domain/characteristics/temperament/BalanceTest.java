package ec.animal.adoption.domain.characteristics.temperament;

import ec.animal.adoption.TestUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BalanceTest {

    @Test
    public void shouldBeAnInstanceOfTemperament() {
        Balance balance = TestUtils.getRandomBalance();

        assertThat(balance, is(instanceOf(Temperament.class)));
    }
}