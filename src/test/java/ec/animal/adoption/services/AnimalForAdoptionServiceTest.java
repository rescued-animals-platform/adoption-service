package ec.animal.adoption.services;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.repositories.AnimalForAdoptionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnimalForAdoptionServiceTest {

    @Mock
    private AnimalForAdoptionRepository animalForAdoptionRepository;

    private AnimalForAdoptionService animalForAdoptionService;

    @Before
    public void setUp() {
        animalForAdoptionService = new AnimalForAdoptionService(animalForAdoptionRepository);
    }

    @Test
    public void shouldCreateAnAnimal() throws EntityAlreadyExistsException {
        AnimalForAdoption expectedAnimalForAdoption = mock(AnimalForAdoption.class);
        AnimalForAdoption animalForAdoption = mock(AnimalForAdoption.class);
        when(animalForAdoptionRepository.save(animalForAdoption)).thenReturn(expectedAnimalForAdoption);

        AnimalForAdoption createdAnimalForAdoption = animalForAdoptionService.create(animalForAdoption);

        assertThat(createdAnimalForAdoption, is(expectedAnimalForAdoption));
    }
}