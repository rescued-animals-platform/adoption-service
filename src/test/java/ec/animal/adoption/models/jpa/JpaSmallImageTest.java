package ec.animal.adoption.models.jpa;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JpaSmallImageTest {

    @Test
    public void shouldCreateAJpaSmallImage() {
        UUID largeImageId = UUID.randomUUID();
        String url = randomAlphabetic(10);
        JpaSmallImage jpaSmallImage = new JpaSmallImage(largeImageId, url);

        assertThat(jpaSmallImage.getLargeImageId(), is(largeImageId));
        assertThat(jpaSmallImage.getUrl(), is(url));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaSmallImage.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).withIgnoredFields("id").verify();
    }
}