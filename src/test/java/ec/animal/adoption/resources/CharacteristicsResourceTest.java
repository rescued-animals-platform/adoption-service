package ec.animal.adoption.resources;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.services.CharacteristicsService;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CharacteristicsResourceTest {

    @Test
    public void shouldGetCharacteristicsForAnimal() throws EntityNotFoundException {
        UUID animalUuid = UUID.randomUUID();
        CharacteristicsService characteristicsService = mock(CharacteristicsService.class);
        Characteristics expectedCharacteristics = mock(Characteristics.class);
        when(characteristicsService.get(animalUuid)).thenReturn(expectedCharacteristics);
        CharacteristicsResource characteristicsResource = new CharacteristicsResource(characteristicsService);

        Characteristics characteristics = characteristicsResource.get(animalUuid);

        assertThat(characteristics, is(expectedCharacteristics));
    }
}
