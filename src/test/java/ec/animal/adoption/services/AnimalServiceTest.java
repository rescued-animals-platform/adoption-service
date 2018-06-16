package ec.animal.adoption.services;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.repositories.AnimalRepository;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalServiceTest {

    @Test
    public void shouldCreateAnAnimal() {
        Animal expectedAnimal = mock(Animal.class);
        Animal animal = mock(Animal.class);
        AnimalRepository animalRepository = mock(AnimalRepository.class);
        when(animalRepository.save(animal)).thenReturn(expectedAnimal);
        AnimalService animalService = new AnimalService(animalRepository);

        Animal createdAnimal = animalService.create(animal);

        assertThat(createdAnimal, is(expectedAnimal));
    }
}