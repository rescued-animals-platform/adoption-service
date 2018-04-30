package ec.animal.adoption.resources;

import ec.animal.adoption.domain.AnimalForAdoption;
import ec.animal.adoption.services.AnimalForAdoptionService;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalForAdoptionResourceTest {

    @Test
    public void shouldCreateAnAnimal() {
        AnimalForAdoption expectedAnimalForAdoption = mock(AnimalForAdoption.class);
        AnimalForAdoption animalForAdoption = mock(AnimalForAdoption.class);
        AnimalForAdoptionService animalForAdoptionService = mock(AnimalForAdoptionService.class);
        when(animalForAdoptionService.create(animalForAdoption)).thenReturn(expectedAnimalForAdoption);
        AnimalForAdoptionResource animalForAdoptionResource = new AnimalForAdoptionResource(animalForAdoptionService);

        AnimalForAdoption createdAnimalForAdoption = animalForAdoptionResource.create(animalForAdoption);

        assertThat(createdAnimalForAdoption, is(expectedAnimalForAdoption));
    }
}