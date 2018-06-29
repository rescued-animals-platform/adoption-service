package ec.animal.adoption.domain.characteristics.temperaments;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.builders.TemperamentsBuilder;
import ec.animal.adoption.helpers.JsonHelper;
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
        Temperaments temperaments = TemperamentsBuilder.random().withSociability(sociability).build();

        assertThat(temperaments.getSociability(), is(sociability));
    }

    @Test
    public void shouldSetDocility() {
        Docility docility = getRandomDocility();
        Temperaments temperaments = TemperamentsBuilder.random().withDocility(docility).build();

        assertThat(temperaments.getDocility(), is(docility));
    }

    @Test
    public void shouldSetBalance() {
        Balance balance = getRandomBalance();
        Temperaments temperaments = TemperamentsBuilder.random().withBalance(balance).build();

        assertThat(temperaments.getBalance(), is(balance));
    }

    @Test
    public void shouldReturnTrueIfTemperamentsIsEmpty() {
        Temperaments temperaments = TemperamentsBuilder.empty().build();

        assertThat(temperaments.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnFalseIfOnlySociabilityIsSet() {
        Temperaments temperaments = TemperamentsBuilder.empty().withSociability(getRandomSociability()).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfOnlyDocilityIsSet() {
        Temperaments temperaments = TemperamentsBuilder.empty().withDocility(getRandomDocility()).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfOnlyBalanceIsSet() {
        Temperaments temperaments = TemperamentsBuilder.empty().withBalance(getRandomBalance()).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfSociabilityAndDocilityAreSet() {
        Temperaments temperaments = TemperamentsBuilder.random().withBalance(null).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfSociabilityAndBalanceAreSet() {
        Temperaments temperaments = TemperamentsBuilder.random().withDocility(null).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldReturnFalseIfDocilityAndBalanceAreSet() {
        Temperaments temperaments = TemperamentsBuilder.random().withSociability(null).build();

        assertThat(temperaments.isEmpty(), is(false));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Temperaments.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void shouldBeSerializableAndDeserializable() throws IOException {
        Temperaments temperaments = TemperamentsBuilder.random().build();
        ObjectMapper objectMapper = JsonHelper.getObjectMapper();

        String serializedTemperaments = objectMapper.writeValueAsString(temperaments);
        Temperaments deserializedTemperaments = objectMapper.readValue(
                serializedTemperaments, Temperaments.class
        );

        assertThat(deserializedTemperaments, is(temperaments));
    }
}