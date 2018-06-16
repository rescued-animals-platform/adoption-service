package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.Picture;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.models.jpa.JpaLinkPicture;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import ec.animal.adoption.repositories.jpa.JpaLinkPictureRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getRandomPictureType;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PictureRepositoryPsqlTest {

    @Mock
    private JpaLinkPictureRepository jpaLinkPictureRepository;

    @Mock
    private JpaAnimalRepository jpaAnimalRepository;

    @Mock
    private JpaAnimal jpaAnimal;

    private PictureRepositoryPsql pictureRepositoryPsql;
    private Picture picture;

    @Before
    public void setUp() {
        picture = new LinkPicture(
                UUID.randomUUID(),
                randomAlphabetic(10),
                getRandomPictureType(),
                new MediaLink(randomAlphabetic(10)),
                new MediaLink(randomAlphabetic(10))
        );
        when(jpaAnimalRepository.findById(picture.getAnimalUuid())).thenReturn(Optional.of(jpaAnimal));
        when(jpaLinkPictureRepository.findByPictureTypeAndAnimalUuid(
                PictureType.PRIMARY.name(), picture.getAnimalUuid())
        ).thenReturn(Optional.empty());
        pictureRepositoryPsql = new PictureRepositoryPsql(jpaLinkPictureRepository, jpaAnimalRepository);
    }

    @Test
    public void shouldBeAnInstanceOfPictureRepository() {
        assertThat(pictureRepositoryPsql, is(instanceOf(PictureRepository.class)));
    }

    @Test
    public void shouldSaveAJpaLinkPicture() {
        ArgumentCaptor<JpaLinkPicture> argumentCaptor = ArgumentCaptor.forClass(JpaLinkPicture.class);
        JpaLinkPicture expectedJpaLinkPicture = new JpaLinkPicture(picture);
        when(jpaLinkPictureRepository.save(any(JpaLinkPicture.class))).thenReturn(expectedJpaLinkPicture);

        Picture savedPicture = pictureRepositoryPsql.save(this.picture);

        verify(jpaLinkPictureRepository).save(argumentCaptor.capture());
        JpaLinkPicture jpaLinkPicture = argumentCaptor.getValue();
        Picture picture = jpaLinkPicture.toPicture();
        assertThat(picture.getAnimalUuid(), is(this.picture.getAnimalUuid()));
        assertThat(picture.getName(), is(this.picture.getName()));
        assertThat(picture.getPictureType(), is(this.picture.getPictureType()));
        assertThat(picture.getLargeImageUrl(), is(this.picture.getLargeImageUrl()));
        assertThat(picture.getSmallImageUrl(), is(this.picture.getSmallImageUrl()));
        assertThat(savedPicture, is(expectedJpaLinkPicture.toPicture()));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistExceptionWhenThereIsAlreadyAPrimaryImageForTheAnimal() {
        when(jpaLinkPictureRepository.findByPictureTypeAndAnimalUuid(
                PictureType.PRIMARY.name(), picture.getAnimalUuid())
        ).thenReturn(Optional.of(mock(JpaLinkPicture.class)));

        pictureRepositoryPsql.save(picture);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistExceptionWhenThereIsADataIntegrityViolation() {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(DataIntegrityViolationException.class);
        }).when(jpaLinkPictureRepository).save(any(JpaLinkPicture.class));

        pictureRepositoryPsql.save(picture);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenCreatingPictureForNonExistentAnimal() {
        when(jpaAnimalRepository.findById(picture.getAnimalUuid())).thenReturn(Optional.empty());

        pictureRepositoryPsql.save(picture);
    }
}