/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.services;

import ec.animal.adoption.domain.characteristics.Characteristics;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsServiceTest {

    @Mock
    private CharacteristicsRepository characteristicsRepository;

    @Mock
    private Characteristics expectedCharacteristics;

    private CharacteristicsService characteristicsService;

    @Before
    public void setUp() {
        characteristicsService = new CharacteristicsService(characteristicsRepository);
    }

    @Test
    public void shouldCreateCharacteristics() {
        Characteristics characteristics = mock(Characteristics.class);
        when(characteristicsRepository.save(characteristics)).thenReturn(expectedCharacteristics);

        Characteristics createdCharacteristics = characteristicsService.create(characteristics);

        assertThat(createdCharacteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldGetCharacteristicsByAnimalUuid() {
        UUID animalUuid = UUID.randomUUID();
        when(characteristicsRepository.getBy(animalUuid)).thenReturn(expectedCharacteristics);

        Characteristics characteristics = characteristicsService.getBy(animalUuid);

        assertThat(characteristics, is(expectedCharacteristics));
    }
}