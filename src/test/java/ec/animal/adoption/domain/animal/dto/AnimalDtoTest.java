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

package ec.animal.adoption.domain.animal.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.PictureType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getObjectMapper;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnimalDtoTest {

    @Test
    public void shouldSerializeAnimalDtoWithPrimaryLinkPicture() throws JsonProcessingException {
        ObjectMapper objectMapper = getObjectMapper();
        UUID animalId = UUID.randomUUID();
        String smallPrimaryPictureUrl = randomAlphabetic(10);
        LinkPicture primaryLinkPicture = LinkPictureBuilder.random()
                                                           .withPictureType(PictureType.PRIMARY)
                                                           .withSmallImageMediaLink(new MediaLink(smallPrimaryPictureUrl))
                                                           .build();
        Animal animal = AnimalBuilder.random().withIdentifier(animalId).withPrimaryLinkPicture(primaryLinkPicture).build();
        AnimalDto animalDto = new AnimalDto(animal);

        String serializedAnimalDto = objectMapper.writeValueAsString(animalDto);

        assertAll(() -> assertTrue(serializedAnimalDto.contains(String.format("\"id\":\"%s\"", animalId.toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"name\":\"%s\"", animal.getName()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"species\":\"%s\"", animal.getSpecies().name()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"estimatedAge\":\"%s\"", animal.getEstimatedAge().getName()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"sex\":\"%s\"", animal.getSex().name()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"smallPrimaryPictureUrl\":\"%s\"", smallPrimaryPictureUrl))));
    }

    @Test
    public void shouldSerializeAnimalDtoWithoutPrimaryLinkPicture() throws JsonProcessingException {
        ObjectMapper objectMapper = getObjectMapper();
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().withIdentifier(animalId).build();
        AnimalDto animalDto = new AnimalDto(animal);

        String serializedAnimalDto = objectMapper.writeValueAsString(animalDto);

        assertAll(() -> assertTrue(serializedAnimalDto.contains(String.format("\"id\":\"%s\"", animalId.toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"name\":\"%s\"", animal.getName()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"species\":\"%s\"", animal.getSpecies().name()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"estimatedAge\":\"%s\"", animal.getEstimatedAge().getName()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"sex\":\"%s\"", animal.getSex().name()))),
                  () -> assertFalse(serializedAnimalDto.contains("\"smallPrimaryPictureUrl\":")));
    }

    @Test
    public void shouldNotBeDeserializable() {
        ObjectMapper objectMapper = getObjectMapper();
        String animalDtoAsJson = "{\"id\":\"32bb12fb-6545-4cba-b439-49bf75b32369\",\"name\":\"LUjMtOrKfE\"," +
                "\"species\":\"CAT\",\"estimatedAge\":\"YOUNG\",\"sex\":\"FEMALE\"," +
                "\"smallPrimaryPictureUrl\":\"TznABoDYFF\"}";

        assertThrows(InvalidDefinitionException.class, () -> {
            objectMapper.readValue(animalDtoAsJson, AnimalDto.class);
        });
    }
}