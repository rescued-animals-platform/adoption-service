package ec.animal.adoption.services;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.exceptions.EntityAlreadyExistsException;
import ec.animal.adoption.exceptions.EntityNotFoundException;
import ec.animal.adoption.repositories.CharacteristicsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsServiceTest {

    @Mock
    private CharacteristicsRepository characteristicsRepository;

    @Mock
    private Characteristics expectedCharacteristics;

    private UUID animalUuid;
    private CharacteristicsService characteristicsService;

    @Before
    public void setUp() {
        animalUuid = UUID.randomUUID();
        characteristicsService = new CharacteristicsService(characteristicsRepository);
    }

    @Test
    public void shouldCreateCharacteristicsForAnimal() throws EntityAlreadyExistsException {
        Characteristics characteristics = mock(Characteristics.class);
        when(characteristicsRepository.save(characteristics)).thenReturn(expectedCharacteristics);

        Characteristics createdCharacteristics = characteristicsService.create(animalUuid, characteristics);

        verify(characteristics).setAnimalUuid(animalUuid);
        assertThat(createdCharacteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldGetCharacteristicsByAnimalUuid() throws EntityNotFoundException {
        when(characteristicsRepository.getBy(animalUuid)).thenReturn(expectedCharacteristics);

        Characteristics characteristics = characteristicsService.get(animalUuid);

        assertThat(characteristics, is(expectedCharacteristics));
    }
}