package ec.animal.adoption.domain.characteristics.temperament;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BalanceTest {

    @Test
    public void shouldBeAnInstanceOfTemperament() {
        Balance balance = getRandomBalance();

        assertThat(balance, is(instanceOf(Temperament.class)));
    }

    private static Balance getRandomBalance() {
        Random random = new Random();
        List<Balance> balanceScales = Arrays.asList(Balance.values());
        int randomBalanceIndex = random.nextInt(balanceScales.size());
        return balanceScales.get(randomBalanceIndex);
    }
}