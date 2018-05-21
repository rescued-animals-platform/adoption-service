package ec.animal.adoption.models.jpa;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JpaTemperamentTest {

    @Test
    public void shouldCreateJpaTemperamentFromTemperament() {
        Temperament temperament = TestUtils.getRandomTemperament();

        JpaTemperament jpaTemperament = new JpaTemperament(temperament);

        assertThat(jpaTemperament.toTemperament(), is(temperament));
    }

    @Test
    public void shouldVerifyEqualsAnsHashCode() {
        EqualsVerifier.forClass(JpaTemperament.class).usingGetClass().verify();
    }
}