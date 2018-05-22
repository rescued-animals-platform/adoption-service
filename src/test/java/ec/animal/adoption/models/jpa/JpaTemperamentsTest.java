package ec.animal.adoption.models.jpa;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class JpaTemperamentsTest {

    @Test
    public void shouldCreateJpaTemperamentFromTemperament() {
        Temperament temperament = TestUtils.getRandomTemperament();

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperament, mock(JpaCharacteristics.class));

        assertThat(jpaTemperaments.toTemperament(), is(temperament));
    }

    @Test
    public void shouldVerifyEqualsAnsHashCode() {
        EqualsVerifier.forClass(JpaTemperaments.class).usingGetClass().withPrefabValues(
                JpaCharacteristics.class,
                mock(JpaCharacteristics.class),
                mock(JpaCharacteristics.class)
        ).verify();
    }
}