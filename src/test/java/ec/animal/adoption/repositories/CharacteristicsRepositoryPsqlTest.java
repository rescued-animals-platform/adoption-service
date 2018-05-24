package ec.animal.adoption.repositories;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import ec.animal.adoption.repositories.jpa.JpaCharacteristicsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsRepositoryPsqlTest {

    @Mock
    private JpaCharacteristicsRepository jpaCharacteristicsRepository;
    
    private CharacteristicsRepositoryPsql characteristicsRepositoryPsql;
    private Characteristics characteristics;
    private UUID animalUuid;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        characteristics = new Characteristics(
                TestUtils.getRandomSize(),
                TestUtils.getRandomPhysicalActivity(),
                Collections.singletonList(TestUtils.getRandomTemperament()),
                TestUtils.getRandomFriendlyWith()
        );
        characteristicsRepositoryPsql = new CharacteristicsRepositoryPsql(jpaCharacteristicsRepository);
    }

    @Test
    public void shouldBeAnInstanceOfCharacteristicsRepository() {
        assertThat(characteristicsRepositoryPsql, is(instanceOf(CharacteristicsRepository.class)));
    }

    @Test
    public void shouldSaveJpaCharacteristics() throws EntityAlreadyExistsException {
        ArgumentCaptor<JpaCharacteristics> jpaCharacteristicsArgumentCaptor = ArgumentCaptor.forClass(
                JpaCharacteristics.class
        );
        characteristics.setAnimalUuid(animalUuid);
        JpaCharacteristics expectedJpaCharacteristics = new JpaCharacteristics(characteristics);
        when(jpaCharacteristicsRepository.save(any(JpaCharacteristics.class))).thenReturn(expectedJpaCharacteristics);

        Characteristics savedCharacteristics = characteristicsRepositoryPsql.save(this.characteristics);

        verify(jpaCharacteristicsRepository).save(jpaCharacteristicsArgumentCaptor.capture());
        JpaCharacteristics jpaCharacteristics = jpaCharacteristicsArgumentCaptor.getValue();
        Characteristics characteristics = jpaCharacteristics.toCharacteristics();

        assertThat(characteristics.getAnimalUuid(), is(this.characteristics.getAnimalUuid()));
        assertThat(characteristics.getSize(), is(this.characteristics.getSize()));
        assertThat(characteristics.getPhysicalActivity(), is(this.characteristics.getPhysicalActivity()));
        assertThat(characteristics.getTemperaments(), is(this.characteristics.getTemperaments()));
        assertThat(characteristics.getFriendlyWith(), is(this.characteristics.getFriendlyWith()));
        assertThat(expectedJpaCharacteristics.toCharacteristics(), is(savedCharacteristics));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistException() throws EntityAlreadyExistsException {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(PSQLException.class);
        }).when(jpaCharacteristicsRepository).save(any(JpaCharacteristics.class));

        characteristicsRepositoryPsql.save(characteristics);
    }

    @Test
    public void shouldGetJpaCharacteristicsByAnimalUuid() throws EntityNotFoundException {
        JpaCharacteristics expectedJpaCharacteristics = new JpaCharacteristics(characteristics);
        when(jpaCharacteristicsRepository.findByAnimalUuid(animalUuid)).thenReturn(expectedJpaCharacteristics);

        Characteristics characteristicsFound = characteristicsRepositoryPsql.getBy(animalUuid);

        assertThat(characteristicsFound, is(characteristics));
        assertThat(characteristicsFound, is(expectedJpaCharacteristics.toCharacteristics()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundException() throws EntityNotFoundException {
        characteristicsRepositoryPsql.getBy(UUID.randomUUID());
    }
}