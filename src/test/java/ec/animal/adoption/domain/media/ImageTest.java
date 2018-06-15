package ec.animal.adoption.domain.media;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static ec.animal.adoption.TestUtils.getRandomSupportedImageExtension;
import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ImageTest {

    private static final String IMAGE_SIZE_IN_BYTES_CAN_NOT_BE_ZERO = "Image size in bytes can't be zero";
    private static final String INVALID_IMAGE = String.format("The image provided doesn't meet one or more of the " +
            "requirements. Supported extensions: %s. Maximum size: 1MB", Arrays.stream(SupportedImageExtension.values())
            .map(SupportedImageExtension::getExtension)
            .collect(Collectors.joining(", ")));

    private Image image;
    private String extension;
    private byte[] content;
    private long sizeInBytes;

    @Before
    public void setUp() {
        SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        extension = supportedImageExtension.getExtension();
        content = supportedImageExtension.getStartingBytes();
        sizeInBytes = new Random().nextInt(10000) + 1;
        image = new Image(extension, content, sizeInBytes);
    }

    @Test
    public void shouldCreateAnImageMedia() {
        assertThat(image.getExtension(), is(extension));
        assertThat(image.getContent(), is(content));
        assertThat(image.getSizeInBytes(), is(sizeInBytes));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Image.class).usingGetClass().verify();
    }

    @Test
    public void shouldValidateWrongImageExtensionOnFilename() {
        String extension = randomAlphabetic(6);
        image = new Image(extension, content, sizeInBytes);

        Set<ConstraintViolation<Image>> constraintViolations = getValidator().validate(image);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Image> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(INVALID_IMAGE));
        assertThat(constraintViolation.getPropertyPath().toString(), is("image"));
    }

    @Test
    public void shouldValidateWrongImageExtensionInContent() {
        byte[] content = {};
        image = new Image(extension, content, sizeInBytes);

        Set<ConstraintViolation<Image>> constraintViolations = getValidator().validate(image);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Image> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(INVALID_IMAGE));
        assertThat(constraintViolation.getPropertyPath().toString(), is("image"));
    }

    @Test
    public void shouldValidateSizeIsBytesIsNotZero() {
        image = new Image(extension, content, 0);

        Set<ConstraintViolation<Image>> constraintViolations = getValidator().validate(image);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Image> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(IMAGE_SIZE_IN_BYTES_CAN_NOT_BE_ZERO));
        assertThat(constraintViolation.getPropertyPath().toString(), is("sizeInBytes"));
    }
}