package ec.animal.adoption.domain.media;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ec.animal.adoption.TestUtils.getRandomSupportedImageExtension;
import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ImageMediaTest {

    private static final String IMAGE_MEDIA_SIZE_IN_BYTES_CAN_NOT_BE_ZERO = "Image media size in bytes can't be zero";
    private static final String INVALID_IMAGE = String.format("The image provided doesn't meet one or more of the " +
            "requirements. Supported extensions: %s. Maximum size: 1MB", Arrays.stream(SupportedImageExtension.values())
            .map(SupportedImageExtension::getExtension)
            .collect(Collectors.joining(", ")));

    private UUID animalUuid;
    private ImageMedia imageMedia;
    private String extension;
    private byte[] content;
    private long sizeInBytes;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        extension = supportedImageExtension.getExtension();
        content = supportedImageExtension.getStartingBytes();
        sizeInBytes = new Random().nextInt(10000) + 1;
        imageMedia = new ImageMedia(animalUuid, extension, content, sizeInBytes);
    }

    @Test
    public void shouldCreateAnImageMedia() {
        assertThat(imageMedia.getAnimalUuid(), is(animalUuid));
        assertThat(imageMedia.getExtension(), is(extension));
        assertThat(imageMedia.getContent(), is(content));
        assertThat(imageMedia.getSizeInBytes(), is(sizeInBytes));
    }

    @Test
    public void shouldReturnName() {
        String expectedFilename = "PrimaryImageLarge." + extension;

        assertThat(imageMedia.getName(), is(expectedFilename));
    }

    @Test
    public void shouldReturnImageMediaPath() {
        String expectedPath = animalUuid + "/" + imageMedia.getName();

        assertThat(imageMedia.getPath(), is(expectedPath));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(ImageMedia.class).usingGetClass().verify();
    }

    @Test
    public void shouldValidateWrongImageExtensionOnFilename() {
        String extension = randomAlphabetic(6);
        imageMedia = new ImageMedia(animalUuid, extension, content, sizeInBytes);

        Set<ConstraintViolation<ImageMedia>> constraintViolations = getValidator().validate(imageMedia);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<ImageMedia> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(INVALID_IMAGE));
        assertThat(constraintViolation.getPropertyPath().toString(), is("image"));
    }

    @Test
    public void shouldValidateWrongImageExtensionInContent() {
        byte[] content = {};
        imageMedia = new ImageMedia(animalUuid, extension, content, sizeInBytes);

        Set<ConstraintViolation<ImageMedia>> constraintViolations = getValidator().validate(imageMedia);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<ImageMedia> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(INVALID_IMAGE));
        assertThat(constraintViolation.getPropertyPath().toString(), is("image"));
    }

    @Test
    public void shouldValidateSizeIsBytesIsNotZero() {
        imageMedia = new ImageMedia(animalUuid, extension, content, 0);

        Set<ConstraintViolation<ImageMedia>> constraintViolations = getValidator().validate(imageMedia);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<ImageMedia> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(IMAGE_MEDIA_SIZE_IN_BYTES_CAN_NOT_BE_ZERO));
        assertThat(constraintViolation.getPropertyPath().toString(), is("sizeInBytes"));
    }
}