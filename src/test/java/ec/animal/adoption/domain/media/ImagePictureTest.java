package ec.animal.adoption.domain.media;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImagePictureTest {

    @Mock
    private Image largeImage;

    @Mock
    private Image smallImage;

    private UUID animalUuid;
    private String name;
    private PictureType pictureType;
    private ImagePicture imagePicture;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        name = randomAlphabetic(10);
        pictureType = getRandomPictureType();
        imagePicture = new ImagePicture(animalUuid, name, pictureType, largeImage, smallImage);
    }

    @Test
    public void shouldCreateAnImagePicture() {
        assertThat(imagePicture.getAnimalUuid(), is(animalUuid));
        assertThat(imagePicture.getName(), is(name));
        assertThat(imagePicture.getPictureType(), is(pictureType));
    }

    @Test
    public void shouldReturnLargeImagePath() {
        String extension = randomAlphabetic(3);
        when(largeImage.getExtension()).thenReturn(extension);
        String expectedLargeImagePath = animalUuid + "/" + name + "_LARGE." + extension;

        assertThat(imagePicture.getLargeImagePath(), is(expectedLargeImagePath));
    }

    @Test
    public void shouldReturnLargeImageContent() {
        byte[] content = {};
        when(largeImage.getContent()).thenReturn(content);

        assertThat(imagePicture.getLargeImageContent(), is(content));
    }

    @Test
    public void shouldReturnSmallImagePath() {
        String extension = randomAlphabetic(3);
        when(smallImage.getExtension()).thenReturn(extension);
        String expectedSmallImagePath = animalUuid + "/" + name + "_SMALL." + extension;

        assertThat(imagePicture.getSmallImagePath(), is(expectedSmallImagePath));
    }

    @Test
    public void shouldReturnSmallImageContent() {
        byte[] content = {};
        when(smallImage.getContent()).thenReturn(content);

        assertThat(imagePicture.getSmallImageContent(), is(content));
    }

    @Test
    public void shouldReturnTrue() {
        assertThat(imagePicture.hasImages(), is(true));
    }

    @Test
    public void shouldReturnFalse() {
        assertThat(imagePicture.hasUrls(), is(false));
    }

    @Test
    public void shouldBeNull() {
        assertNull(imagePicture.getLargeImageUrl());
        assertNull(imagePicture.getSmallImageUrl());
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(ImagePicture.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }
}