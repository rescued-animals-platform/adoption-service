package ec.animal.adoption.domain.characteristics.temperament;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DocilityTest {

    @Test
    public void shouldBeAnInstanceOfTemperament() {
        Docility docility = getRandomDocility();

        assertThat(docility, is(instanceOf(Temperament.class)));
    }

    private static Docility getRandomDocility() {
        Random random = new Random();
        List<Docility> docilityScales = Arrays.asList(Docility.values());
        int randomDocilityIndex = random.nextInt(docilityScales.size());
        return docilityScales.get(randomDocilityIndex);
    }
}