package ec.animal.adoption.resources;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.services.CharacteristicsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsResourceTest {

    @Mock
    private CharacteristicsService characteristicsService;

    @Mock
    private Characteristics expectedCharacteristics;

    private UUID animalUuid;
    private CharacteristicsResource characteristicsResource;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        characteristicsResource = new CharacteristicsResource(characteristicsService);
    }

    @Test
    public void shouldCreateCharacteristicsForAnimal() throws EntityAlreadyExistsException {
        Characteristics characteristics = mock(Characteristics.class);
        when(characteristicsService.create(animalUuid, characteristics)).thenReturn(expectedCharacteristics);

        Characteristics createdCharacteristics = characteristicsResource.create(animalUuid, characteristics);

        assertThat(createdCharacteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldGetCharacteristicsForAnimal() throws EntityNotFoundException {
        when(characteristicsService.get(animalUuid)).thenReturn(expectedCharacteristics);

        Characteristics characteristics = characteristicsResource.get(animalUuid);

        assertThat(characteristics, is(expectedCharacteristics));
    }
}
