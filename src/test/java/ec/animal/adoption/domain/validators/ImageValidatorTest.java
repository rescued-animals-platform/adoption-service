package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.picture.Image;
import ec.animal.adoption.domain.picture.SupportedImageExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.InputStream;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ImageValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private InputStream image;

    private ImageValidator imageValidator;

    @Before
    public void setUp() {
        imageValidator = new ImageValidator();
    }

    @Test
    public void shouldBeAnInstanceOfConstraintValidator() {
        assertThat(imageValidator, is(instanceOf(ConstraintValidator.class)));
    }

    @Test
    public void shouldBeValidForJpeg() {
        byte[] bytes = SupportedImageExtension.JPEG.getStartingBytes();
        Image image = new Image(this.image, bytes);

        assertThat(imageValidator.isValid(image, context), is(true));
    }

    @Test
    public void shouldBeValidForPng() {
        byte[] bytes = SupportedImageExtension.PNG.getStartingBytes();
        Image image = new Image(this.image, bytes);

        assertThat(imageValidator.isValid(image, context), is(true));
    }

    @Test
    public void shouldBeInvalid() {
        byte[] bytes = {};
        Image image = new Image(this.image, bytes);

        assertThat(imageValidator.isValid(image, context), is(false));
    }
}