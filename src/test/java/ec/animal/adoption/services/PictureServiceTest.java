package ec.animal.adoption.services;

import ec.animal.adoption.domain.picture.Picture;
import ec.animal.adoption.repositories.PictureRepository;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PictureServiceTest {

    @Test
    public void shouldCreateAPicture() {
        Picture expectedPicture = mock(Picture.class);
        Picture picture = mock(Picture.class);
        PictureRepository pictureRepository = mock(PictureRepository.class);
        when(pictureRepository.save(picture)).thenReturn(expectedPicture);
        PictureService pictureService = new PictureService(pictureRepository);

        Picture createdPicture = pictureService.create(picture);

        assertThat(createdPicture, is(expectedPicture));
    }

}