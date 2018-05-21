package ec.animal.adoption.services;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.repositories.CharacteristicsRepository;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CharacteristicsServiceTest {

    @Test
    public void shouldGetCharacteristicsByAnimalUuid() {
        CharacteristicsRepository characteristicsRepository = mock(CharacteristicsRepository.class);
        UUID animalUuid = UUID.randomUUID();
        Characteristics expectedCharacteristics = mock(Characteristics.class);
        when(characteristicsRepository.getBy(animalUuid)).thenReturn(expectedCharacteristics);
        CharacteristicsService characteristicsService = new CharacteristicsService(characteristicsRepository);

        Characteristics characteristics = characteristicsService.get(animalUuid);

        assertThat(characteristics, is(expectedCharacteristics));
    }
}