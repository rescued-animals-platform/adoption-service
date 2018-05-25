package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class JpaCharacteristicsTest {

    @Test
    public void shouldCreateJpaCharacteristicsFromCharacteristics() {
        Characteristics characteristics = new Characteristics(
                getRandomSize(),
                getRandomPhysicalActivity(),
                new Temperaments(getRandomSociability(), getRandomDocility(), getRandomBalance()),
                getRandomFriendlyWith()
        );
        characteristics.setAnimalUuid(UUID.randomUUID());
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics);

        assertThat(jpaCharacteristics.toCharacteristics(), is(characteristics));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaCharacteristics.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).withPrefabValues(
                JpaFriendlyWith.class, mock(JpaFriendlyWith.class), mock(JpaFriendlyWith.class)
        ).verify();
    }
}