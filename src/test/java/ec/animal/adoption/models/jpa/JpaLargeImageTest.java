package ec.animal.adoption.models.jpa;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomImageType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class JpaLargeImageTest {

    @Test
    public void shouldCreateAJpaLargeImage() {
        UUID animalUuid = UUID.randomUUID();
        String imageType = getRandomImageType().name();
        String name = randomAlphabetic(10);
        String url = randomAlphabetic(10);
        JpaLargeImage jpaLargeImage = new JpaLargeImage(
                animalUuid, imageType, name, url
        );

        assertNotNull(jpaLargeImage.getId());
        assertThat(jpaLargeImage.getAnimalUuid(), is(animalUuid));
        assertThat(jpaLargeImage.getImageType(), is(imageType));
        assertThat(jpaLargeImage.getName(), is(name));
        assertThat(jpaLargeImage.getUrl(), is(url));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaLargeImage.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).withIgnoredFields("id").verify();
    }
}