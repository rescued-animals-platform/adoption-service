package ec.animal.adoption.resources;

import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.services.AnimalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnimalResourceTest {

    @Mock
    private AnimalService animalService;

    @Mock
    private Animal animal;

    private AnimalResource animalResource;

    @Before
    public void setUp() {
        animalResource = new AnimalResource(animalService);
    }

    @Test
    public void shouldCreateANewAnimal() {
        animalResource.create(animal);

        verify(animalService).create(animal);
    }

    @Test
    public void shouldReturnTheCreatedAnimal() {
        Animal expectedAnimal = mock(Animal.class);
        when(animalService.create(animal)).thenReturn(expectedAnimal);
        Animal createdAnimal = animalResource.create(animal);

        assertThat(createdAnimal, is(expectedAnimal));
    }
}