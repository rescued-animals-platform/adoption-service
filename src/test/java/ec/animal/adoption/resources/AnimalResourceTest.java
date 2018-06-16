package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.services.AnimalService;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalResourceTest {

    @Test
    public void shouldCreateAnAnimal() {
        Animal expectedAnimal = mock(Animal.class);
        Animal animal = mock(Animal.class);
        AnimalService animalService = mock(AnimalService.class);
        when(animalService.create(animal)).thenReturn(expectedAnimal);
        AnimalResource animalResource = new AnimalResource(animalService);

        Animal createdAnimal = animalResource.create(animal);

        assertThat(createdAnimal, is(expectedAnimal));
    }
}