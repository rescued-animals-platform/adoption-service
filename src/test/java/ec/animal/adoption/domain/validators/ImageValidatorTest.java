/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

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