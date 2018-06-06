package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Picture;
import ec.animal.adoption.services.PictureService;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PictureResourceTest {

    @Test
    public void shouldCreateAPicture() {
        Picture expectedPicture = mock(Picture.class);
        Picture picture = mock(Picture.class);
        PictureService pictureService = mock(PictureService.class);
        when(pictureService.create(picture)).thenReturn(expectedPicture);
        PictureResource pictureResource = new PictureResource(pictureService);

        Picture createdPicture = pictureResource.create(picture);

        assertThat(createdPicture, is(expectedPicture));
    }
}