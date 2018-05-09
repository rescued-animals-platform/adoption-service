package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JpaStateTest {

    private State state;

    @Before
    public void setUp() {
        state = new LookingForHuman(LocalDateTime.now());
    }

    @Test
    public void shouldCreateAJpaStateFromAState() {
        JpaState jpaState = new JpaState(state);

        assertThat(jpaState.toState(), is(state));
    }

    @Test
    public void shouldChangeState() {
        JpaState jpaState = new JpaState(state);
        Unavailable newState = new Unavailable(randomAlphabetic(10));
        jpaState.setState(newState);

        assertThat(jpaState.toState(), is(newState));
    }

    @Test
    public void shouldBeEqualToAJpaStateWithSameValues() {
        JpaState JpaState = new JpaState(state);
        JpaState sameJpaState = new JpaState(state);

        assertEquals(JpaState, sameJpaState);
    }

    @Test
    public void shouldNotBeEqualToAJpaStateWithDifferentValues() {
        JpaState JpaState = new JpaState(state);
        State anotherState = new Unavailable(randomAlphabetic(10));
        JpaState anotherJpaState = new JpaState(anotherState);

        assertNotEquals(JpaState, anotherJpaState);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        JpaState JpaState = new JpaState(state);

        assertNotEquals(JpaState, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        JpaState JpaState = new JpaState(state);

        assertNotEquals(JpaState, null);
    }

    @Test
    public void shouldBeEqualToItself() {
        JpaState JpaState = new JpaState(state);

        assertEquals(JpaState, JpaState);
    }

    @Test
    public void shouldBeEqual() {
        JpaState jpaState = new JpaState();
        JpaState sameJpaState = new JpaState();

        assertEquals(jpaState, sameJpaState);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        JpaState JpaState = new JpaState(state);
        JpaState sameJpaState = new JpaState(state);

        assertEquals(JpaState.hashCode(), sameJpaState.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeWhenHavingDifferentValues() {
        JpaState JpaState = new JpaState(state);
        State anotherState = new Adopted(LocalDate.now(), randomAlphabetic(10));
        JpaState anotherJpaState = new JpaState(anotherState);

        assertNotEquals(JpaState.hashCode(), anotherJpaState.hashCode());
    }
}