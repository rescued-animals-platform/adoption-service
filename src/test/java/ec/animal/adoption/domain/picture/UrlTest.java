package ec.animal.adoption.domain.picture;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.net.URI;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UrlTest {

    @Test
    public void shouldBeAnInstanceOfPictureRepresentation() {
        Url url = new Url(URI.create(randomAlphabetic(10)));

        assertThat(url, is(instanceOf(PictureRepresentation.class)));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Url.class).usingGetClass().verify();
    }
}