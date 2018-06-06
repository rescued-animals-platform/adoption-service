package ec.animal.adoption.domain.picture;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MediaLinkTest {

    @Test
    public void shouldBeAnInstanceOfPictureRepresentation() {
        MediaLink mediaLink = new MediaLink(randomAlphabetic(10));

        assertThat(mediaLink, is(instanceOf(PictureRepresentation.class)));
    }

    @Test
    public void shouldSetUrl() {
        String url = randomAlphabetic(10);
        MediaLink mediaLink = new MediaLink(url);

        assertThat(mediaLink.getUrl(), is(url));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(MediaLink.class).usingGetClass().verify();
    }
}