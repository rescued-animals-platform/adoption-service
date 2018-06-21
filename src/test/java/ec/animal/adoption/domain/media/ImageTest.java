package ec.animal.adoption.domain.media;

import ec.animal.adoption.builders.ImageBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
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

    @Test
    public void shouldCreateAnImageMedia() {
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
    public void shouldValidateWrongImageExtensionOnFilename() {
        Image image = ImageBuilder.random().withExtension(randomAlphabetic(6)).build();

        Set<ConstraintViolation<Image>> constraintViolations = getValidator().validate(image);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Image> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(INVALID_IMAGE));
        assertThat(constraintViolation.getPropertyPath().toString(), is("image"));
    }

    @Test
    public void shouldValidateWrongImageExtensionInContent() {
        Image image = ImageBuilder.random().withContent(new byte[]{}).build();

        Set<ConstraintViolation<Image>> constraintViolations = getValidator().validate(image);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Image> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(INVALID_IMAGE));
        assertThat(constraintViolation.getPropertyPath().toString(), is("image"));
    }

    @Test
    public void shouldValidateSizeIsBytesIsNotZero() {
        Image image = ImageBuilder.random().withSizeInBytes(0).build();

        Set<ConstraintViolation<Image>> constraintViolations = getValidator().validate(image);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Image> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(IMAGE_SIZE_IN_BYTES_CAN_NOT_BE_ZERO));
        assertThat(constraintViolation.getPropertyPath().toString(), is("sizeInBytes"));
    }
}