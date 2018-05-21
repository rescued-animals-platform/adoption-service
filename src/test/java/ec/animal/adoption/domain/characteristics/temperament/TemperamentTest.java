package ec.animal.adoption.domain.characteristics.temperament;

import ec.animal.adoption.TestUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemperamentTest {

    @Test
    public void shouldReturnSociabilityFromName() {
        Sociability randomSociability = TestUtils.getRandomSociability();

        Temperament temperament = Temperament.valueOf(randomSociability.name());

        assertThat(temperament, is(randomSociability));
    }

    @Test
    public void shouldReturnDocilityFromName() {
        Docility randomDocility = TestUtils.getRandomDocility();

        Temperament temperament = Temperament.valueOf(randomDocility.name());

        assertThat(temperament, is(randomDocility));
    }

    @Test
    public void shouldReturnBalanceFromName() {
        Balance randomBalance = TestUtils.getRandomBalance();

        Temperament temperament = Temperament.valueOf(randomBalance.name());

        assertThat(temperament, is(randomBalance));
    }
}