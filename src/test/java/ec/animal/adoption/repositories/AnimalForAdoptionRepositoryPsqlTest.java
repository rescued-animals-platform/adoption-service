package ec.animal.adoption.repositories;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.models.JpaAnimalForAdoption;
import ec.animal.adoption.repositories.jpa.JpaAnimalForAdoptionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnimalForAdoptionRepositoryPsqlTest {

    @Mock
    private JpaAnimalForAdoptionRepository jpaAnimalForAdoptionRepository;

    private AnimalForAdoptionRepositoryPsql animalForAdoptionRepositoryPsql;

    @Before
    public void setUp() {
        animalForAdoptionRepositoryPsql = new AnimalForAdoptionRepositoryPsql(jpaAnimalForAdoptionRepository);
    }

    @Test
    public void shouldBeAnInstanceOfAnimalRepository() {
        assertThat(animalForAdoptionRepositoryPsql, is(instanceOf(AnimalForAdoptionRepository.class)));
    }

    @Test
    public void shouldSaveJpaAvailableAnimal() {
        ArgumentCaptor<JpaAnimalForAdoption> jpaAnimalArgumentCaptor = ArgumentCaptor.forClass(JpaAnimalForAdoption.class);
        AnimalForAdoption animalForAdoption = new AnimalForAdoption(
                randomAlphabetic(10),
                randomAlphabetic(10),
                LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()))
        );
        JpaAnimalForAdoption expectedJpaAnimalForAdoption = new JpaAnimalForAdoption(animalForAdoption);
        when(jpaAnimalForAdoptionRepository.save(any(JpaAnimalForAdoption.class))).thenReturn(expectedJpaAnimalForAdoption);

        AnimalForAdoption savedAnimalForAdoption = animalForAdoptionRepositoryPsql.save(animalForAdoption);

        verify(jpaAnimalForAdoptionRepository).save(jpaAnimalArgumentCaptor.capture());
        JpaAnimalForAdoption jpaAnimalForAdoption = jpaAnimalArgumentCaptor.getValue();
        assertThat(jpaAnimalForAdoption, is(expectedJpaAnimalForAdoption));
        assertThat(jpaAnimalForAdoption.toAvailableAnimal(), is(savedAnimalForAdoption));
    }
}
