package ec.animal.adoption.services;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.repositories.AnimalForAdoptionRepository;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalForAdoptionServiceTest {
    
    @Test
    public void shouldCreateAnAnimal() {
        AnimalForAdoption expectedAnimalForAdoption = mock(AnimalForAdoption.class);
        AnimalForAdoption animalForAdoption = mock(AnimalForAdoption.class);
        AnimalForAdoptionRepository animalForAdoptionRepository = mock(AnimalForAdoptionRepository.class);
        when(animalForAdoptionRepository.save(animalForAdoption)).thenReturn(expectedAnimalForAdoption);
        AnimalForAdoptionService animalForAdoptionService = new AnimalForAdoptionService(animalForAdoptionRepository);

        AnimalForAdoption createdAnimalForAdoption = animalForAdoptionService.create(animalForAdoption);

        assertThat(createdAnimalForAdoption, is(expectedAnimalForAdoption));
    }
}