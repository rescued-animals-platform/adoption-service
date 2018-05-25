package ec.animal.adoption.domain.characteristics.temperaments;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.io.IOException;

import static ec.animal.adoption.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TemperamentsTest {

    @Test
    public void shouldSetSociability() {
        Sociability sociability = getRandomSociability();
        Temperaments temperaments = new Temperaments(sociability, getRandomDocility(), getRandomBalance());

        assertThat(temperaments.getSociability(), is(sociability));
    }

    @Test
    public void shouldSetDocility() {
        Docility docility = getRandomDocility();
        Temperaments temperaments = new Temperaments(getRandomSociability(), docility, getRandomBalance());

        assertThat(temperaments.getDocility(), is(docility));
    }

    @Test
    public void shouldSetBalance() {
        Balance balance = getRandomBalance();
        Temperaments temperaments = new Temperaments(getRandomSociability(), getRandomDocility(), balance);

        assertThat(temperaments.getBalance(), is(balance));
    }

    @Test
    public void shouldReturnTrueIfTemperamentsIsEmpty() {
        Temperaments temperaments = new Temperaments(null, null, null);

        assertThat(temperaments.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnFalseIfOnlySociabilityIsSet() {
        Temperaments temperaments = new Temperaments(getRandomSociability(), null, null);

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfOnlyDocilityIsSet() {
        Temperaments temperaments = new Temperaments(null, getRandomDocility(), null);

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfOnlyBalanceIsSet() {
        Temperaments temperaments = new Temperaments(null, null, getRandomBalance());

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfSociabilityAndDocilityAreSet() {
        Temperaments temperaments = new Temperaments(getRandomSociability(), getRandomDocility(), null);

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfSociabilityAndBalanceAreSet() {
        Temperaments temperaments = new Temperaments(getRandomSociability(), null, getRandomBalance());

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfDocilityAndBalanceAreSet() {
        Temperaments temperaments = new Temperaments(null, getRandomDocility(), getRandomBalance());

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Temperaments.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        Temperaments temperaments = new Temperaments(getRandomSociability(), getRandomDocility(), getRandomBalance());
        ObjectMapper objectMapper = new ObjectMapper();

        String serializedTemperaments = objectMapper.writeValueAsString(temperaments);
        Temperaments deserializedTemperaments = objectMapper.readValue(
                serializedTemperaments, Temperaments.class
        );

        assertThat(deserializedTemperaments, is(temperaments));
    }
}