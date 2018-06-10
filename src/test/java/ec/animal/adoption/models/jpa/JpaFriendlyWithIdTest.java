package ec.animal.adoption.models.jpa;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.io.Serializable;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class JpaFriendlyWithIdTest {

    private JpaFriendlyWithId jpaFriendlyWithId;

    @Test
    public void shouldBeAnInstanceOfSerializable() {
        jpaFriendlyWithId = new JpaFriendlyWithId(randomAlphabetic(10), mock(JpaCharacteristics.class));

        assertThat(jpaFriendlyWithId, is(instanceOf(Serializable.class)));
    }

    @Test
    public void shouldCreateJpaFriendlyWithIdWithFriendlyWith() {
        String friendlyWith = randomAlphabetic(10);
        jpaFriendlyWithId = new JpaFriendlyWithId(friendlyWith, mock(JpaCharacteristics.class));

        assertThat(jpaFriendlyWithId.getFriendlyWith(), is(friendlyWith));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaFriendlyWithId.class).usingGetClass().withPrefabValues(
                JpaCharacteristics.class,
                mock(JpaCharacteristics.class),
                mock(JpaCharacteristics.class)
        ).suppress(Warning.REFERENCE_EQUALITY, Warning.NONFINAL_FIELDS).verify();
    }
}