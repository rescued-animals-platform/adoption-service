package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class LookingForHumanTest {

    private LocalDateTime registrationDate;
    private LookingForHuman lookingForHumanState;

    @Before
    public void setUp() {
        registrationDate = LocalDateTime.now();
        lookingForHumanState = new LookingForHuman(registrationDate);
    }

    @Test
    public void shouldBeAnInstanceOfState() {
        assertThat(lookingForHumanState, is(instanceOf(State.class)));
    }

    @Test
    public void shouldHaveRegistrationDate() {
        assertThat(lookingForHumanState.getRegistrationDate(), is(registrationDate));
    }

    @Test
    public void shouldBeEqualToLookingForHumanStateWithSameValues() {
        LookingForHuman sameLookingForHumanState = new LookingForHuman(registrationDate);

        assertEquals(lookingForHumanState, sameLookingForHumanState);
    }

    @Test
    public void shouldNotBeEqualToLookingForHumanStateWithDifferentValues() {
        LookingForHuman differentLookingForHumanState = new LookingForHuman(LocalDateTime.now().minusDays(3));

        assertNotEquals(lookingForHumanState, differentLookingForHumanState);
    }

    @Test
    public void shouldBeEqualToItself() {
        assertEquals(lookingForHumanState, lookingForHumanState);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        assertNotEquals(lookingForHumanState, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertNotEquals(lookingForHumanState, null);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        LookingForHuman sameLookingForHumanState = new LookingForHuman(registrationDate);

        assertEquals(lookingForHumanState.hashCode(), sameLookingForHumanState.hashCode());
    }

    @Test
    public void shouldNotHaveSameHashCodeWhenHavingDifferentValues() {
        LookingForHuman differentLookingForHumanState = new LookingForHuman(LocalDateTime.now().minusDays(4));

        assertNotEquals(lookingForHumanState.hashCode(), differentLookingForHumanState.hashCode());
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedLookingForHumanState = objectMapper.writeValueAsString(lookingForHumanState);
        LookingForHuman deserializedLookingForHumanState = objectMapper.readValue(serializedLookingForHumanState, LookingForHuman.class);

        assertThat(deserializedLookingForHumanState, is(lookingForHumanState));
    }
}