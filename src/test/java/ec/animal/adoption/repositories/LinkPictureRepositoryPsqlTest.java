package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.PictureType;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaLinkPicture;
import ec.animal.adoption.repositories.jpa.JpaLinkPictureRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class LinkPictureRepositoryPsqlTest {

    @Mock
    private JpaLinkPictureRepository jpaLinkPictureRepository;

    @Mock
    private AnimalRepositoryPsql animalRepositoryPsql;

    private LinkPictureRepositoryPsql linkPictureRepositoryPsql;
    private LinkPicture linkPicture;

    @Before
    public void setUp() {
        linkPicture = new LinkPicture(
                UUID.randomUUID(),
                randomAlphabetic(10),
                getRandomPictureType(),
                new MediaLink(randomAlphabetic(10)),
                new MediaLink(randomAlphabetic(10))
        );
        when(animalRepositoryPsql.animalExists(linkPicture.getAnimalUuid())).thenReturn(true);
        when(jpaLinkPictureRepository.findByPictureTypeAndAnimalUuid(
                PictureType.PRIMARY.name(), linkPicture.getAnimalUuid())
        ).thenReturn(Optional.empty());
        linkPictureRepositoryPsql = new LinkPictureRepositoryPsql(jpaLinkPictureRepository, animalRepositoryPsql);
    }

    @Test
    public void shouldBeAnInstanceOfPictureRepository() {
        assertThat(linkPictureRepositoryPsql, is(instanceOf(LinkPictureRepository.class)));
    }

    @Test
    public void shouldSaveAJpaLinkPicture() {
        JpaLinkPicture expectedJpaLinkPicture = new JpaLinkPicture(linkPicture);
        when(jpaLinkPictureRepository.save(any(JpaLinkPicture.class))).thenReturn(expectedJpaLinkPicture);

        LinkPicture savedLinkPicture = linkPictureRepositoryPsql.save(linkPicture);

        assertThat(savedLinkPicture.getAnimalUuid(), is(linkPicture.getAnimalUuid()));
        assertThat(savedLinkPicture.getName(), is(linkPicture.getName()));
        assertThat(savedLinkPicture.getPictureType(), is(linkPicture.getPictureType()));
        assertThat(savedLinkPicture.getLargeImageUrl(), is(linkPicture.getLargeImageUrl()));
        assertThat(savedLinkPicture.getSmallImageUrl(), is(linkPicture.getSmallImageUrl()));
        assertThat(savedLinkPicture, is(expectedJpaLinkPicture.toPicture()));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistExceptionWhenThereIsAlreadyAPrimaryImageForTheAnimal() {
        when(jpaLinkPictureRepository.findByPictureTypeAndAnimalUuid(
                PictureType.PRIMARY.name(), linkPicture.getAnimalUuid())
        ).thenReturn(Optional.of(mock(JpaLinkPicture.class)));

        linkPictureRepositoryPsql.save(linkPicture);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistExceptionWhenThereIsADataIntegrityViolation() {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(DataIntegrityViolationException.class);
        }).when(jpaLinkPictureRepository).save(any(JpaLinkPicture.class));

        linkPictureRepositoryPsql.save(linkPicture);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenCreatingPictureForNonExistentAnimal() {
        when(animalRepositoryPsql.animalExists(linkPicture.getAnimalUuid())).thenReturn(false);

        linkPictureRepositoryPsql.save(linkPicture);
    }
}