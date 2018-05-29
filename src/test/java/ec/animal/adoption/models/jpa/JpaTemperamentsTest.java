package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static ec.animal.adoption.TestUtils.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

public class JpaTemperamentsTest {

    @Test
    public void shouldCreateJpaTemperamentFromTemperament() {
        Temperaments temperaments = new Temperaments(getRandomSociability(), getRandomDocility(), getRandomBalance());

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperaments);

        assertThat(jpaTemperaments.toTemperaments(), is(temperaments));
    }

    @Test
    public void shouldAcceptNullSociability() {
        Temperaments temperaments = new Temperaments(null, getRandomDocility(), getRandomBalance());

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperaments);

        assertNull(jpaTemperaments.toTemperaments().getSociability());
    }

    @Test
    public void shouldAcceptNullDocility() {
        Temperaments temperaments = new Temperaments(getRandomSociability(), null, getRandomBalance());

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperaments);

        assertNull(jpaTemperaments.toTemperaments().getDocility());
    }

    @Test
    public void shouldAcceptNullBalance() {
        Temperaments temperaments = new Temperaments(getRandomSociability(), getRandomDocility(), null);

        JpaTemperaments jpaTemperaments = new JpaTemperaments(temperaments);

        assertNull(jpaTemperaments.toTemperaments().getBalance());
    }

    @Test
    public void shouldVerifyEqualsAnsHashCode() {
        EqualsVerifier.forClass(JpaTemperaments.class).usingGetClass().verify();
    }
}