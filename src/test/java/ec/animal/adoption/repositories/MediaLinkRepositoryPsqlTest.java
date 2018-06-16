package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.jpa.JpaMediaLink;
import ec.animal.adoption.repositories.jpa.JpaMediaLinkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MediaLinkRepositoryPsqlTest {

    @Mock
    private JpaMediaLinkRepository jpaMediaLinkRepository;

    private MediaLink mediaLink;
    private MediaLinkRepositoryPsql mediaLinkRepositoryPsql;


    @Before
    public void setUp() {
        mediaLink = new MediaLink(UUID.randomUUID(), randomAlphabetic(10), randomAlphabetic(10));
        mediaLinkRepositoryPsql = new MediaLinkRepositoryPsql(jpaMediaLinkRepository);
    }

    @Test
    public void shouldBeAnInstanceOfMediaLinkRepository() {
        assertThat(mediaLinkRepositoryPsql, is(instanceOf(MediaLinkRepository.class)));
    }

    @Test
    public void shouldSaveJpaMediaLink() throws EntityAlreadyExistsException {
        ArgumentCaptor<JpaMediaLink> jpaImageMediaArgumentCaptor = ArgumentCaptor.forClass(JpaMediaLink.class);
        JpaMediaLink expectedJpaMediaLink = new JpaMediaLink(this.mediaLink);
        when(jpaMediaLinkRepository.save(any(JpaMediaLink.class))).thenReturn(expectedJpaMediaLink);

        MediaLink savedMediaLink = mediaLinkRepositoryPsql.save(mediaLink);

        verify(jpaMediaLinkRepository).save(jpaImageMediaArgumentCaptor.capture());
        JpaMediaLink jpaMediaLink = jpaImageMediaArgumentCaptor.getValue();
        MediaLink mediaLink = jpaMediaLink.toMediaLink();

        assertThat(mediaLink, is(this.mediaLink));
        assertThat(expectedJpaMediaLink.toMediaLink(), is(savedMediaLink));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistException() throws EntityAlreadyExistsException {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(PSQLException.class);
        }).when(jpaMediaLinkRepository).save(any(JpaMediaLink.class));

        mediaLinkRepositoryPsql.save(this.mediaLink);
    }
}