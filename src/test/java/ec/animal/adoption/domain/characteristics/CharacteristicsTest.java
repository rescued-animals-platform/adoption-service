package ec.animal.adoption.domain.characteristics;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.temperament.Temperament;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class CharacteristicsTest {

    private Temperament temperament;
    private Size size;
    private PhysicalActivity physicalActivity;
    private List<Temperament> temperaments;

    @Before
    public void setUp() {
        size = TestUtils.getRandomSize();
        physicalActivity = TestUtils.getRandomPhysicalActivity();
        temperament = TestUtils.getRandomTemperament();
        temperaments = newArrayList(temperament);
    }

    @Test
    public void shouldEliminateDuplicatesOnFriendlyWith() {
        Characteristics expectedCharacteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );

        Characteristics characteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );

        assertThat(characteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldEliminateDuplicatesOnTemperaments() {
        Temperament anotherTemperament = mock(Temperament.class);
        List<Temperament> temperaments = newArrayList(temperament, anotherTemperament, anotherTemperament);
        Characteristics expectedCharacteristics = new Characteristics(
                size, physicalActivity, newArrayList(temperament, anotherTemperament)
        );

        Characteristics characteristics = new Characteristics(size, physicalActivity, temperaments);

        assertThat(characteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Characteristics.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        Characteristics characteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );
        ObjectMapper objectMapper = new ObjectMapper();

        String serializedCharacteristics = objectMapper.writeValueAsString(characteristics);
        Characteristics deserializedCharacteristics = objectMapper.readValue(
                serializedCharacteristics, Characteristics.class
        );

        assertThat(deserializedCharacteristics, is(characteristics));
    }
}