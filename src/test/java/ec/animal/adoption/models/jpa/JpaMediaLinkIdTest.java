package ec.animal.adoption.models.jpa;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.io.Serializable;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JpaMediaLinkIdTest {

    private JpaMediaLinkId jpaMediaLinkId;

    @Test
    public void shouldBeAnInstanceOfSerializable() {
        jpaMediaLinkId = new JpaMediaLinkId(UUID.randomUUID(), randomAlphabetic(10));

        assertThat(jpaMediaLinkId, is(instanceOf(Serializable.class)));
    }

    @Test
    public void shouldCreateJpaMediaLinkIdWithAnimalUuidAndMediaName() {
        UUID animalUuid = UUID.randomUUID();
        String mediaName = randomAlphabetic(10);
        jpaMediaLinkId = new JpaMediaLinkId(animalUuid, mediaName);

        assertThat(jpaMediaLinkId.getAnimalUuid(), is(animalUuid));
        assertThat(jpaMediaLinkId.getMediaName(), is(mediaName));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaMediaLinkId.class).usingGetClass().suppress(Warning.NONFINAL_FIELDS).verify();
    }
}