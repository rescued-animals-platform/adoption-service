package ec.animal.adoption.domain.picture;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Set;

import static ec.animal.adoption.TestUtils.getValidator;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PictureTest {

    private static final String PICTURE_NAME_IS_REQUIRED = "Picture name is required";
    private static final String PICTURE_SIZE_IN_BYTES_CAN_NOT_BE_ZERO = "Picture size in bytes can't be zero";
    private static final String UNSUPPORTED_FILE_EXTENSION = "Unsupported file extension";

    @Mock
    private Image validImage;

    private String name;
    private String filename;
    private long sizeInBytes;

    @Before
    public void setUp() {
        name = randomAlphabetic(10);
        filename = randomAlphabetic(10) + ".jpeg";
        sizeInBytes = new Random().nextInt(10000) + 1;
        when(validImage.getBytes()).thenReturn(SupportedImageExtension.JPEG.getStartingBytes());
    }

    @Test
    public void shouldCreateAPictureWithAnImage() {
        InputStream inputStream = mock(InputStream.class);
        PictureRepresentation image = new Image(inputStream, new byte[]{});
        Picture picture = new Picture(name, filename, sizeInBytes, image);

        assertThat(picture.getName(), is(name));
        assertThat(picture.getFilename(), is(filename));
        assertThat(picture.getSizeInBytes(), is(sizeInBytes));
        assertTrue(picture.getInputStream().isPresent());
        assertThat(picture.getInputStream().get(), is(inputStream));
    }

    @Test
    public void shouldReturnEmptyInputStreamWhenAPictureWasCreatedWithAMediaLinkPictureRepresentation() {
        Picture picture = new Picture(name, filename, sizeInBytes, new MediaLink(randomAlphabetic(10)));

        assertFalse(picture.getInputStream().isPresent());
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Picture.class).usingGetClass().verify();
    }

    @Test
    public void shouldBeJsonDeserializableWithNameFilenameAndMediaLink() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String url = randomAlphabetic(10);
        String serializedPicture = "{\"name\":\"" + name + "\",\"filename\":\"" + filename +
                "\",\"sizeInBytes\":\"" + sizeInBytes + "\",\"mediaLink\":{\"url\":\"" + url + "\"}}";
        Picture expectedPicture = new Picture(name, filename, sizeInBytes, new MediaLink(url));

        Picture picture = objectMapper.readValue(serializedPicture, Picture.class);

        assertThat(picture, is(expectedPicture));
    }

    @Test
    public void shouldValidateNonNullName() {
        Picture picture = new Picture(null, filename, sizeInBytes, validImage);

        Set<ConstraintViolation<Picture>> constraintViolations = getValidator().validate(picture);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Picture> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(PICTURE_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonEmptyName() {
        Picture picture = new Picture("", filename, sizeInBytes, validImage);

        Set<ConstraintViolation<Picture>> constraintViolations = getValidator().validate(picture);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Picture> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(PICTURE_NAME_IS_REQUIRED));
        assertThat(constraintViolation.getPropertyPath().toString(), is("name"));
    }

    @Test
    public void shouldValidateNonNullFilename() {
        Picture picture = new Picture(name, null, sizeInBytes, validImage);

        Set<ConstraintViolation<Picture>> constraintViolations = getValidator().validate(picture);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Picture> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(UNSUPPORTED_FILE_EXTENSION));
        assertThat(constraintViolation.getPropertyPath().toString(), is("filename"));
    }

    @Test
    public void shouldValidateNonEmptyFilename() {
        Picture picture = new Picture(name, "", sizeInBytes, validImage);

        Set<ConstraintViolation<Picture>> constraintViolations = getValidator().validate(picture);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Picture> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(UNSUPPORTED_FILE_EXTENSION));
        assertThat(constraintViolation.getPropertyPath().toString(), is("filename"));
    }

    @Test
    public void shouldValidateWrongImageExtensionOnFilename() {
        String filename = randomAlphabetic(10);
        Picture picture = new Picture(name, filename, sizeInBytes, validImage);

        Set<ConstraintViolation<Picture>> constraintViolations = getValidator().validate(picture);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Picture> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(UNSUPPORTED_FILE_EXTENSION));
        assertThat(constraintViolation.getPropertyPath().toString(), is("filename"));
    }

    @Test
    public void shouldValidateSizeIsNotZero() {
        Picture picture = new Picture(name, filename, 0, validImage);

        Set<ConstraintViolation<Picture>> constraintViolations = getValidator().validate(picture);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Picture> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(PICTURE_SIZE_IN_BYTES_CAN_NOT_BE_ZERO));
        assertThat(constraintViolation.getPropertyPath().toString(), is("sizeInBytes"));
    }

    @Test
    public void shouldValidateWrongImageExtensionOnImageBytes() {
        Image invalidImage = mock(Image.class);
        when(invalidImage.getBytes()).thenReturn(new byte[]{});
        Picture picture = new Picture(name, filename, sizeInBytes, invalidImage);

        Set<ConstraintViolation<Picture>> constraintViolations = getValidator().validate(picture);

        assertThat(constraintViolations.size(), is(1));
        ConstraintViolation<Picture> constraintViolation = constraintViolations.iterator().next();
        assertThat(constraintViolation.getMessage(), is(UNSUPPORTED_FILE_EXTENSION));
        assertThat(constraintViolation.getPropertyPath().toString(), is("pictureRepresentation"));
    }

    @Test
    public void shouldBeValid() {
        Picture picture = new Picture(name, filename, sizeInBytes, validImage);

        Set<ConstraintViolation<Picture>> constraintViolations = getValidator().validate(picture);

        assertThat(constraintViolations.size(), is(0));
    }
}