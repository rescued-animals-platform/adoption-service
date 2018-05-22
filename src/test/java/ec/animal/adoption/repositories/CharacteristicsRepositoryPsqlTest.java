package ec.animal.adoption.repositories;

import ec.animal.adoption.TestUtils;
import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.models.jpa.JpaCharacteristics;
import ec.animal.adoption.repositories.jpa.JpaCharacteristicsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsRepositoryPsqlTest {

    @Mock
    private JpaCharacteristicsRepository jpaCharacteristicsRepository;
    
    private CharacteristicsRepositoryPsql characteristicsRepositoryPsql;

    @Before
    public void setUp() {
        characteristicsRepositoryPsql = new CharacteristicsRepositoryPsql(jpaCharacteristicsRepository);
    }

    @Test
    public void shouldBeAnInstanceOfCharacteristicsRepository() {
        assertThat(characteristicsRepositoryPsql, is(instanceOf(CharacteristicsRepository.class)));
    }

    @Test
    public void shouldGetJpaCharacteristicsByAnimalUuid() throws EntityNotFoundException {
        UUID animalUuid = UUID.randomUUID();
        Characteristics characteristics = new Characteristics(
                TestUtils.getRandomSize(),
                TestUtils.getRandomPhysicalActivity(),
                Collections.singletonList(TestUtils.getRandomTemperament()),
                TestUtils.getRandomFriendlyWith()
        );
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