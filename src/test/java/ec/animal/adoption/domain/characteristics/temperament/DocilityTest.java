package ec.animal.adoption.domain.characteristics.temperament;

import ec.animal.adoption.TestUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DocilityTest {

    @Test
    public void shouldBeAnInstanceOfTemperament() {
        Docility docility = TestUtils.getRandomDocility();

        assertThat(docility, is(instanceOf(Temperament.class)));
    }
}