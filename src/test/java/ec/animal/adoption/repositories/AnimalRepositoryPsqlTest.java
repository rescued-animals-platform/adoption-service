package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Type;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.models.jpa.JpaAnimal;
import ec.animal.adoption.repositories.jpa.JpaAnimalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.postgresql.util.PSQLException;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnimalRepositoryPsqlTest {

    @Mock
    private JpaAnimalRepository jpaAnimalRepository;

    private AnimalRepositoryPsql animalRepositoryPsql;
    private Animal animal;

    @Before
    public void setUp() {
        LocalDateTime registrationDate = LocalDateTime.now();
        animal = new Animal(
                randomAlphabetic(10),
                randomAlphabetic(10),
                registrationDate,
                Type.DOG,
                EstimatedAge.YOUNG_ADULT,
                new LookingForHuman(registrationDate)
        );

        animalRepositoryPsql = new AnimalRepositoryPsql(jpaAnimalRepository);
    }

    @Test
    public void shouldBeAnInstanceOfAnimalRepository() {
        assertThat(animalRepositoryPsql, is(instanceOf(AnimalRepository.class)));
    }

    @Test
    public void shouldSaveJpaAnimal() throws EntityAlreadyExistsException {
        ArgumentCaptor<JpaAnimal> jpaAnimalArgumentCaptor = ArgumentCaptor.forClass(JpaAnimal.class);
        JpaAnimal expectedJpaAnimal = new JpaAnimal(animal);
        when(jpaAnimalRepository.save(any(JpaAnimal.class))).thenReturn(expectedJpaAnimal);

        Animal savedAnimal = animalRepositoryPsql.save(animal);

        verify(jpaAnimalRepository).save(jpaAnimalArgumentCaptor.capture());
        JpaAnimal jpaAnimal = jpaAnimalArgumentCaptor.getValue();
        assertThat(jpaAnimal, is(expectedJpaAnimal));
        assertThat(jpaAnimal.toAnimal(), is(savedAnimal));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void shouldThrowEntityAlreadyExistException() throws EntityAlreadyExistsException {
        doAnswer((Answer<Object>) invocation -> {
            throw mock(PSQLException.class);
        }).when(jpaAnimalRepository).save(any(JpaAnimal.class));

        animalRepositoryPsql.save(animal);
    }
}
