package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class UnavailableTest {

    private String notes;
    private Unavailable unavailableState;

    @Before
    public void setUp() {
        notes = randomAlphabetic(20);
        unavailableState = new Unavailable(notes);
    }

    @Test
    public void shouldHaveNotes() {
        assertThat(unavailableState.getNotes(), is(notes));
    }

    @Test
    public void shouldBeEqualToUnavailableStateWithSameValues() {
        Unavailable sameUnavailableState = new Unavailable(notes);

        assertEquals(unavailableState, sameUnavailableState);
    }

    @Test
    public void shouldNotBeEqualToUnavailableStateWithDifferentValues() {
        Unavailable differentUnavailableState = new Unavailable(randomAlphabetic(20));

        assertNotEquals(unavailableState, differentUnavailableState);
    }

    @Test
    public void shouldBeEqualToItself() {
        assertEquals(unavailableState, unavailableState);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        assertNotEquals(unavailableState, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertNotEquals(unavailableState, null);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        Unavailable sameUnavailableState = new Unavailable(notes);

        assertEquals(unavailableState.hashCode(), sameUnavailableState.hashCode());
    }

    @Test
    public void shouldNotHaveSameHashCodeWhenHavingDifferentValues() {
        Unavailable differentUnavailableState = new Unavailable(randomAlphabetic(20));

        assertNotEquals(unavailableState.hashCode(), differentUnavailableState.hashCode());
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedUnavailableState = objectMapper.writeValueAsString(unavailableState);
        Unavailable deserializedUnavailableState = objectMapper.readValue(serializedUnavailableState, Unavailable.class);

        assertThat(deserializedUnavailableState, is(unavailableState));
    }
}