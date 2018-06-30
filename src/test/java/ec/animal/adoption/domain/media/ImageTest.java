package ec.animal.adoption.domain.media;

import ec.animal.adoption.builders.ImageBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static ec.animal.adoption.TestUtils.getRandomSupportedImageExtension;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ImageTest {

    private static final String IMAGE_SIZE_IN_BYTES_CAN_NOT_BE_ZERO = "Image size in bytes can't be zero";
    private static final String INVALID_IMAGE = String.format("The image provided doesn't meet one or more of the " +
            "requirements. Supported extensions: %s. Maximum size: 1MB", Arrays.stream(SupportedImageExtension.values())
            .map(SupportedImageExtension::getExtension)
            .collect(Collectors.joining(", ")));

    @Test
    public void shouldCreateAnImage() {
        SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        long sizeInBytes = new Random().nextInt(100) + 1;
        Image image = ImageBuilder.random(). withSupportedImageExtension(supportedImageExtension).
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
    public void shouldBeValidForJpegAndSizeLessThanOneMb() {
        SupportedImageExtension jpeg = SupportedImageExtension.JPEG;
        Image image = ImageBuilder.random().withSupportedImageExtension(jpeg).build();

        assertThat(image.isValid(), is(true));
    }

    @Test
    public void shouldBeValidForJpgAndSizeLessThanOneMb() {
        SupportedImageExtension jpg = SupportedImageExtension.JPG;
        Image image = ImageBuilder.random().withSupportedImageExtension(jpg).build();

        assertThat(image.isValid(), is(true));
    }

    @Test
    public void shouldBeValidForPngAndSizeLessThanOneMb() {
        SupportedImageExtension png = SupportedImageExtension.PNG;
        Image image = ImageBuilder.random().withSupportedImageExtension(png).build();

        assertThat(image.isValid(), is(true));
    }

    @Test
    public void shouldBeValidForSizeEqualToOneMb() {
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
        int oneMegaByteInBytes = 1048576;
        int size = oneMegaByteInBytes + 1 + new Random().nextInt(100);
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
}