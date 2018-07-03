package ec.animal.adoption.domain.media;

import ec.animal.adoption.builders.ImageBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Random;

import static ec.animal.adoption.TestUtils.getRandomSupportedImageExtension;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ImageTest {

    @Test
    public void shouldCreateAnImage() {
        SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        long sizeInBytes = new Random().nextInt(100) + 1;
        Image image = ImageBuilder.random().withSupportedImageExtension(supportedImageExtension).
                withSizeInBytes(sizeInBytes).build();

        assertThat(image.getExtension(), is(supportedImageExtension.getExtension()));
        assertThat(image.getContent(), is(supportedImageExtension.getStartingBytes()));
        assertThat(image.getSizeInBytes(), is(sizeInBytes));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Image.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeValidForValidImageContentAndExtensionAndSizeBetweenZeroAndOneMb() {
        Image image = ImageBuilder.random().build();

        assertThat(image.isValid(), is(true));
    }

    @Test
    public void shouldBeValidForValidImageContentAndExtensionAndSizeEqualToOneMb() {
        int oneMegaByteInBytes = 1048576;
        Image image = ImageBuilder.random().withSizeInBytes(oneMegaByteInBytes).build();

        assertThat(image.isValid(), is(true));
    }

    @Test
    public void shouldBeInvalidForSizeGreaterThanOneMb() {
        int oneMegaByteInBytes = 1048576;
        int size = oneMegaByteInBytes + 1 + new Random().nextInt(100);
        Image image = ImageBuilder.random().withSizeInBytes(size).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    public void shouldBeInvalidForSizeEqualsToZero() {
        Image image = ImageBuilder.random().withSizeInBytes(0).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    public void shouldBeInvalidForSizeLessThanZero() {
        int size = -5000;
        Image image = ImageBuilder.random().withSizeInBytes(size).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    public void shouldBeInvalidWithRightExtensionInFilenameButWrongImageTypeInContent() {
        byte[] invalidContent = {};
        Image image = ImageBuilder.random().withContent(invalidContent).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    public void shouldBeInvalidWithRightImageTypeInContentButWrongExtensionInFilename() {
        String wrongExtension = randomAlphabetic(10);
        Image image = ImageBuilder.random().withExtension(wrongExtension).build();

        assertThat(image.isValid(), is(false));
    }

    @Test
    public void shouldBeInvalidWithWrongImageTypeInContentAndWrongExtensionInFilename() {
        String wrongExtension = randomAlphabetic(10);
        byte[] invalidContent = {};
        Image image = ImageBuilder.random().withExtension(wrongExtension).withContent(invalidContent).build();

        assertThat(image.isValid(), is(false));
    }
}