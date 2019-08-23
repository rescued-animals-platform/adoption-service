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

package ec.animal.adoption.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import ec.animal.adoption.builders.AnimalBuilder;
import ec.animal.adoption.builders.LinkPictureBuilder;
import ec.animal.adoption.domain.Animal;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.PictureType;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static ec.animal.adoption.TestUtils.getObjectMapper;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class AnimalDtoTest {

    @Test
    public void shouldSerializeAnimalDtoWithPrimaryLinkPicture() throws JsonProcessingException {
        ObjectMapper objectMapper = getObjectMapper();
        UUID uuid = UUID.randomUUID();
        String smallPrimaryPictureUrl = randomAlphabetic(10);
        Animal animal = AnimalBuilder.random().withUuid(uuid).withPrimaryLinkPicture(LinkPictureBuilder.random()
                .withPictureType(PictureType.PRIMARY).withSmallImageMediaLink(new MediaLink(smallPrimaryPictureUrl))
                .build()).build();
        AnimalDto animalDto = new AnimalDto(animal);

        String serializedAnimalDto = objectMapper.writeValueAsString(animalDto);

        assertThat(serializedAnimalDto, containsString(String.format("\"uuid\":\"%s\"", uuid.toString())));
        assertThat(serializedAnimalDto, containsString(String.format("\"name\":\"%s\"", animal.getName())));
        assertThat(serializedAnimalDto, containsString(String.format("\"species\":\"%s\"", animal.getSpecies().name())));
        assertThat(serializedAnimalDto, containsString(String.format("\"estimatedAge\":\"%s\"",
                animal.getEstimatedAge().name())));
        assertThat(serializedAnimalDto, containsString(String.format("\"sex\":\"%s\"", animal.getSex().name())));
        assertThat(serializedAnimalDto, containsString(String.format("\"smallPrimaryPictureUrl\":\"%s\"",
                smallPrimaryPictureUrl)));
    }

    @Test
    public void shouldSerializeAnimalDtoWithoutPrimaryLinkPicture() throws JsonProcessingException {
        ObjectMapper objectMapper = getObjectMapper();
        UUID uuid = UUID.randomUUID();
        Animal animal = AnimalBuilder.random().withUuid(uuid).build();
        AnimalDto animalDto = new AnimalDto(animal);

        String serializedAnimalDto = objectMapper.writeValueAsString(animalDto);

        assertThat(serializedAnimalDto, containsString(String.format("\"uuid\":\"%s\"", uuid.toString())));
        assertThat(serializedAnimalDto, containsString(String.format("\"name\":\"%s\"", animal.getName())));
        assertThat(serializedAnimalDto, containsString(String.format("\"species\":\"%s\"", animal.getSpecies().name())));
        assertThat(serializedAnimalDto, containsString(String.format("\"estimatedAge\":\"%s\"",
                animal.getEstimatedAge().name())));
        assertThat(serializedAnimalDto, containsString(String.format("\"sex\":\"%s\"", animal.getSex().name())));
        assertThat(serializedAnimalDto, not(containsString("smallPrimaryPictureUrl")));
    }

    @Test(expected = InvalidDefinitionException.class)
    public void shouldNotBeDeserializable() throws IOException {
        ObjectMapper objectMapper = getObjectMapper();
        String animalDtoAsJson = "{\"uuid\":\"32bb12fb-6545-4cba-b439-49bf75b32369\",\"name\":\"LUjMtOrKfE\"," +
                "\"species\":\"CAT\",\"estimatedAge\":\"YOUNG\",\"sex\":\"FEMALE\"," +
                "\"smallPrimaryPictureUrl\":\"TznABoDYFF\"}";

        objectMapper.readValue(animalDtoAsJson, AnimalDto.class);
    }
}