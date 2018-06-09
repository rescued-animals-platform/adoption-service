package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.media.ImageMedia;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import static javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageMediaValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder constraintViolationBuilder;

    @Mock
    private NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

    private UUID animalUuid;
    private long size;
    private ImageMediaValidator imageMediaValidator;

    @Before
    public void setUp() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext);
        animalUuid = UUID.randomUUID();
        size = new Random().nextLong();
        imageMediaValidator = new ImageMediaValidator();
    }

    @Test
    public void shouldBeAnInstanceOfConstraintValidator() {
        assertThat(imageMediaValidator, is(instanceOf(ConstraintValidator.class)));
    }

    @Test
    public void shouldBeValidForJpeg() {
        SupportedImageExtension jpeg = SupportedImageExtension.JPEG;
        ImageMedia imageMedia = new ImageMedia(animalUuid, jpeg.getExtension(), jpeg.getStartingBytes(), size);

        assertThat(imageMediaValidator.isValid(imageMedia, context), is(true));
    }

    @Test
    public void shouldBeValidForJpg() {
        SupportedImageExtension jpg = SupportedImageExtension.JPG;
        ImageMedia imageMedia = new ImageMedia(animalUuid, jpg.getExtension(), jpg.getStartingBytes(), size);

        assertThat(imageMediaValidator.isValid(imageMedia, context), is(true));
    }

    @Test
    public void shouldBeValidForPng() {
        SupportedImageExtension png = SupportedImageExtension.PNG;
        ImageMedia imageMedia = new ImageMedia(animalUuid, png.getExtension(), png.getStartingBytes(), size);

        assertThat(imageMediaValidator.isValid(imageMedia, context), is(true));
    }

    @Test
    public void shouldBeInvalidWithRightExtensionInFilenameButWrongImageTypeInContent() {
        byte[] invalidContent = {};
        SupportedImageExtension[] supportedImageExtensions = SupportedImageExtension.values();
        int randomSupportedImageExtensionIndex = new Random().nextInt(supportedImageExtensions.length);
        String extension = supportedImageExtensions[randomSupportedImageExtensionIndex].getExtension();
        ImageMedia imageMedia = new ImageMedia(animalUuid, extension, invalidContent, size);

        assertThat(imageMediaValidator.isValid(imageMedia, context), is(false));
    }

    @Test
    public void shouldBeInvalidWithRightImageTypeInContentButWrongExtensionInFilename() {
        String wrongExtension = randomAlphabetic(10);
        SupportedImageExtension[] supportedImageExtensions = SupportedImageExtension.values();
        int randomSupportedImageExtensionIndex = new Random().nextInt(supportedImageExtensions.length);
        byte[] content = supportedImageExtensions[randomSupportedImageExtensionIndex].getStartingBytes();
        ImageMedia imageMedia = new ImageMedia(animalUuid, wrongExtension, content, size);

        assertThat(imageMediaValidator.isValid(imageMedia, context), is(false));
    }

    @Test
    public void shouldCustomizeContextWhenInvalid() {
        ImageMedia imageMedia = new ImageMedia(animalUuid, randomAlphabetic(10), new byte[]{}, size);
        String expectedTemplate = "Unsupported file extension. Supported extensions are: ".concat(
                Arrays.stream(SupportedImageExtension.values()).map(SupportedImageExtension::getExtension).
                        collect(Collectors.joining(", "))
        );

        imageMediaValidator.isValid(imageMedia, context);

        verify(context, times(1)).disableDefaultConstraintViolation();
        verify(context, times(1)).buildConstraintViolationWithTemplate(expectedTemplate);
        verify(constraintViolationBuilder, times(1)).addPropertyNode("image");
        verify(nodeBuilderCustomizableContext, times(1)).addConstraintViolation();
    }
}