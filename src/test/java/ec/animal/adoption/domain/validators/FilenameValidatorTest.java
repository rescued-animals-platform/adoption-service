package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.picture.SupportedImageExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FilenameValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private FilenameValidator filenameValidator;

    @Before
    public void setUp() {
        filenameValidator = new FilenameValidator();
    }

    @Test
    public void shouldBeAnInstanceOfConstraintValidator() {
        assertThat(filenameValidator, is(instanceOf(ConstraintValidator.class)));
    }

    @Test
    public void shouldBeValid() {
        List<String> supportedExtensions = SupportedImageExtension.getSupportedExtensions();
        String randomSupportedExtension = supportedExtensions.get(new Random().nextInt(supportedExtensions.size()));
        String filename = randomAlphabetic(10) + "." + randomSupportedExtension;

        assertThat(filenameValidator.isValid(filename, context), is(true));
    }

    @Test
    public void shouldBeInvalidIfFilenameIsNull() {
        assertThat(filenameValidator.isValid(null, context), is(false));
    }

    @Test
    public void shouldBeInvalidIfFilenameIsEmpty() {
        assertThat(filenameValidator.isValid("", context), is(false));
    }

    @Test
    public void shouldBeInvalidIfFilenameDoesNotHaveExtension() {
        assertThat(filenameValidator.isValid(randomAlphabetic(10), context), is(false));
    }

    @Test
    public void shouldBeInvalidIfFilenameHasUnsupportedExtension() {
        String filename = randomAlphabetic(10) + ".invalidExtension";

        assertThat(filenameValidator.isValid(filename, context), is(false));
    }
}