package ec.animal.adoption.models.jpa;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.FriendlyWith;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class JpaFriendlyWithTest {

    @Test
    public void shouldCreateJpaTemperamentFromTemperament() {
        FriendlyWith friendlyWith = TestUtils.getRandomFriendlyWith();

        JpaFriendlyWith jpaFriendlyWith = new JpaFriendlyWith(friendlyWith, mock(JpaCharacteristics.class));

        assertThat(jpaFriendlyWith.toFriendlyWith(), is(friendlyWith));
    }

    @Test
    public void shouldVerifyEqualsAnsHashCode() {
        EqualsVerifier.forClass(JpaFriendlyWith.class).usingGetClass().withPrefabValues(
                JpaCharacteristics.class,
                mock(JpaCharacteristics.class),
                mock(JpaCharacteristics.class)
        ).verify();
    }
}