package ec.animal.adoption.domain.media;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinkPictureTest {

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(LinkPicture.class).usingGetClass().verify();
    }
}