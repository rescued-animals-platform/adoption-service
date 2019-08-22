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

package ec.animal.adoption.resources;

import ec.animal.adoption.domain.characteristics.Characteristics;
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
    public void shouldCreateCharacteristicsForAnimal() {
        Characteristics characteristics = mock(Characteristics.class);
        when(characteristicsService.create(animalUuid, characteristics)).thenReturn(expectedCharacteristics);

        Characteristics createdCharacteristics = characteristicsResource.create(animalUuid, characteristics);

        assertThat(createdCharacteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldGetCharacteristicsForAnimal() {
        when(characteristicsService.getBy(animalUuid)).thenReturn(expectedCharacteristics);

        Characteristics characteristics = characteristicsResource.get(animalUuid);

        assertThat(characteristics, is(expectedCharacteristics));
    }
}
