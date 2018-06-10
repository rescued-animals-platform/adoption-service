package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.media.MediaLink;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JpaMediaLinkTest {

    @Test
    public void shouldCreateJpaMediaLinkFromMediaLink() {
        MediaLink mediaLink = new MediaLink(UUID.randomUUID(), randomAlphabetic(10), randomAlphabetic(10));
        JpaMediaLink jpaMediaLink = new JpaMediaLink(mediaLink);

        assertThat(jpaMediaLink.toMediaLink(), is(mediaLink));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaMediaLink.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).verify();
    }
}