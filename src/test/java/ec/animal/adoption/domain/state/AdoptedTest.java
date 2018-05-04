package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDate;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class AdoptedTest {

    private LocalDate adoptionDate;
    private String adoptionFormId;
    private Adopted adoptedState;

    @Before
    public void setUp() {
        adoptionDate = LocalDate.now();
        adoptionFormId = randomAlphabetic(10);
        adoptedState = new Adopted(adoptionDate, adoptionFormId);
    }

    @Test
    public void shouldBeAnInstanceOfState() {
        assertThat(adoptedState, is(instanceOf(State.class)));
    }

    @Test
    public void shouldHaveAnAdoptionDate() {
        assertThat(adoptedState.getAdoptionDate(), is(adoptionDate));
    }

    @Test
    public void shouldHaveAdoptionFormId() {
        assertThat(adoptedState.getAdoptionFormId(), is(adoptionFormId));
    }

    @Test
    public void shouldBeEqualToAdoptedStateWithSameValues() {
        Adopted sameAdoptedState = new Adopted(adoptionDate, adoptionFormId);

        assertEquals(adoptedState, sameAdoptedState);
    }

    @Test
    public void shouldNotBeEqualToAdoptedStateWithDifferentValues() {
        Adopted differentAdoptedState = new Adopted(LocalDate.now().minusDays(3), randomAlphabetic(10));

        assertNotEquals(adoptedState, differentAdoptedState);
    }

    @Test
    public void shouldBeEqualToItself() {
        assertEquals(adoptedState, adoptedState);
    }

    @Test
    public void shouldNotBeEqualToAnotherObject() {
        assertNotEquals(adoptedState, new Object());
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertNotEquals(adoptedState, null);
    }

    @Test
    public void shouldHaveSameHashCodeWhenHavingSameValues() {
        Adopted sameAdoptedState = new Adopted(adoptionDate, adoptionFormId);

        assertEquals(adoptedState.hashCode(), sameAdoptedState.hashCode());
    }

    @Test
    public void shouldNotHaveSameHashCodeWhenHavingDifferentValues() {
        Adopted differentAdoptedState = new Adopted(LocalDate.now().minusDays(4), randomAlphabetic(10));

        assertNotEquals(adoptedState.hashCode(), differentAdoptedState.hashCode());
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        String serializedAdoptedState = objectMapper.writeValueAsString(adoptedState);
        Adopted deserializedAdoptedState = objectMapper.readValue(serializedAdoptedState, Adopted.class);

        assertThat(deserializedAdoptedState, is(adoptedState));
    }
}