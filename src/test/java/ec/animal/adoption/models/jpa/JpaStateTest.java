package ec.animal.adoption.models.jpa;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JpaStateTest {

    private State state;

    @Before
    public void setUp() {
        state = TestUtils.getRandomState();
    }

    @Test
    public void shouldCreateAJpaStateFromAState() {
        JpaState jpaState = new JpaState(state);

        assertThat(jpaState.toState(), is(state));
    }

    @Test
    public void shouldChangeState() {
        JpaState jpaState = new JpaState(new LookingForHuman(LocalDateTime.now()));
        Unavailable newState = new Unavailable(randomAlphabetic(10));

        jpaState.setState(newState);

        assertThat(jpaState.toState(), is(newState));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaState.class).usingGetClass().verify();
    }
}