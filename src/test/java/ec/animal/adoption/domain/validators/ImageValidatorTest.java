package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.SupportedImageExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static ec.animal.adoption.TestUtils.getRandomSupportedImageExtension;
import static javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import static javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageValidatorTest {

    private static final String INVALID_MESSAGE = "The image provided doesn't meet one or more of the requirements. " +
            "Supported extensions: %s. Maximum size: 1MB";

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder constraintViolationBuilder;

    @Mock
    private NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

    private int oneMegaByteInBytes;
    private long size;
    private ImageValidator imageValidator;

    @Before
    public void setUp() {
        oneMegaByteInBytes = 1048576;
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext);
        size = new Random().nextInt(oneMegaByteInBytes);
        imageValidator = new ImageValidator();
    }

    @Test
    public void shouldBeAnInstanceOfConstraintValidator() {
        assertThat(imageValidator, is(instanceOf(ConstraintValidator.class)));
    }

    @Test
    public void shouldBeValidForJpegAndSizeLessThanOneMb() {
        SupportedImageExtension jpeg = SupportedImageExtension.JPEG;
        Image image = new Image(jpeg.getExtension(), jpeg.getStartingBytes(), size);

        assertThat(imageValidator.isValid(image, context), is(true));
    }

    @Test
    public void shouldBeValidForJpgAndSizeLessThanOneMb() {
        SupportedImageExtension jpg = SupportedImageExtension.JPG;
        Image image = new Image(jpg.getExtension(), jpg.getStartingBytes(), size);

        assertThat(imageValidator.isValid(image, context), is(true));
    }

    @Test
    public void shouldBeValidForPngAndSizeLessThanOneMb() {
        SupportedImageExtension png = SupportedImageExtension.PNG;
        Image image = new Image(png.getExtension(), png.getStartingBytes(), size);

        assertThat(imageValidator.isValid(image, context), is(true));
    }

    @Test
    public void shouldBeValidForSizeEqualToOneMb() {
        SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        Image image = new Image(
                supportedImageExtension.getExtension(),
                supportedImageExtension.getStartingBytes(),
                oneMegaByteInBytes
        );

        assertThat(imageValidator.isValid(image, context), is(true));
    }

    @Test
    public void shouldBeInvalidForSizeGreaterThanOneMb() {
        SupportedImageExtension supportedImageExtension = getRandomSupportedImageExtension();
        size = oneMegaByteInBytes + new Random().nextInt(100);
        Image image = new Image(
                supportedImageExtension.getExtension(), supportedImageExtension.getStartingBytes(), size
        );

        assertThat(imageValidator.isValid(image, context), is(false));
    }

    @Test
    public void shouldBeInvalidWithRightExtensionInFilenameButWrongImageTypeInContent() {
        byte[] invalidContent = {};
        String extension = getRandomSupportedImageExtension().getExtension();
        Image image = new Image(extension, invalidContent, size);

        assertThat(imageValidator.isValid(image, context), is(false));
    }

    @Test
    public void shouldBeInvalidWithRightImageTypeInContentButWrongExtensionInFilename() {
        String wrongExtension = randomAlphabetic(10);
        byte[] content = getRandomSupportedImageExtension().getStartingBytes();
        Image image = new Image(wrongExtension, content, size);

        assertThat(imageValidator.isValid(image, context), is(false));
    }

    @Test
    public void shouldCustomizeContextWhenInvalid() {
        Image image = new Image(randomAlphabetic(10), new byte[]{}, size);
        String expectedTemplate = String.format(INVALID_MESSAGE, Arrays.stream(SupportedImageExtension.values())
                .map(SupportedImageExtension::getExtension)
                .collect(Collectors.joining(", ")));

        imageValidator.isValid(image, context);

        verify(context, times(1)).disableDefaultConstraintViolation();
        verify(context, times(1)).buildConstraintViolationWithTemplate(expectedTemplate);
        verify(constraintViolationBuilder, times(1)).addPropertyNode("image");
        verify(nodeBuilderCustomizableContext, times(1)).addConstraintViolation();
    }
}