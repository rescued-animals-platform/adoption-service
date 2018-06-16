package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.Picture;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JpaLinkPictureTest {

    @Test
    public void shouldCreateJpaLinkPictureFromLinkPicture() {
        LinkPicture linkPicture = new LinkPicture(
                UUID.randomUUID(),
                randomAlphabetic(10),
                getRandomPictureType(),
                new MediaLink(randomAlphabetic(10)),
                new MediaLink(randomAlphabetic(10))
        );
        JpaLinkPicture jpaLinkPicture = new JpaLinkPicture(linkPicture);

        assertThat(jpaLinkPicture.toPicture(), is(linkPicture));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNotCreatedWithLinkPicture() {
        Picture picture = mock(Picture.class);
        when(picture.hasUrls()).thenReturn(false);

        new JpaLinkPicture(picture);
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaLinkPicture.class).usingGetClass().withPrefabValues(
                Timestamp.class,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusDays(2))
        ).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}