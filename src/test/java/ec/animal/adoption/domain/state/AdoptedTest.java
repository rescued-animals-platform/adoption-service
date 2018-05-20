package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDate;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AdoptedTest {

    private Adopted adoptedState;

    @Before
    public void setUp() {
        LocalDate adoptionDate = LocalDate.now();
        String adoptionFormId = randomAlphabetic(10);
        adoptedState = new Adopted(adoptionDate, adoptionFormId);
    }

    @Test
    public void shouldBeAnInstanceOfState() {
        assertThat(adoptedState, is(instanceOf(State.class)));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Adopted.class).usingGetClass().verify();
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