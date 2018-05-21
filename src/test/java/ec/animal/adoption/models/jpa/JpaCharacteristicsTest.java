package ec.animal.adoption.models.jpa;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.Characteristics;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JpaCharacteristicsTest {

    @Test
    public void shouldCreateJpaCharacteristicsFromCharacteristics() {
        Characteristics characteristics = new Characteristics(
                TestUtils.getRandomSize(),
                TestUtils.getRandomPhysicalActivity(),
                Collections.singletonList(TestUtils.getRandomTemperament()),
                TestUtils.getRandomFriendlyWith()
        );
        JpaCharacteristics jpaCharacteristics = new JpaCharacteristics(characteristics);

        assertThat(jpaCharacteristics.toCharacteristics(), is(characteristics));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaCharacteristics.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).verify();
    }
}