package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.services.AnimalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AnimalResourceTest {

    @Mock
    private AnimalService animalService;

    @Mock
    private Animal animal;

    @Test
    public void shouldCreateANewAnimal() {
        AnimalResource animalResource = new AnimalResource(animalService);

        animalResource.create(animal);

        verify(animalService).create(animal);
    }
}