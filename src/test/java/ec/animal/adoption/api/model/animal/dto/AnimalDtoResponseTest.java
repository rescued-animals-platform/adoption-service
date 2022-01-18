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

package ec.animal.adoption.api.model.animal.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalFactory;
import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.LinkPictureFactory;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.PictureType;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static ec.animal.adoption.TestUtils.getObjectMapper;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalDtoResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = getObjectMapper();
    }

    @Test
    public void shouldSerializeAnimalDtoWithPrimaryLinkPicture() throws JsonProcessingException {
        UUID animalId = UUID.randomUUID();
        String smallPrimaryPictureUrl = randomAlphabetic(10);
        MediaLink smallImageMediaLink = new MediaLink(randomAlphabetic(10), smallPrimaryPictureUrl);
        LinkPicture primaryLinkPicture = LinkPictureFactory.random()
                                                           .withPictureType(PictureType.PRIMARY)
                                                           .withSmallImageMediaLink(smallImageMediaLink)
                                                           .build();
        Animal animal = AnimalFactory.random().withIdentifier(animalId).withPrimaryLinkPicture(primaryLinkPicture).build();
        AnimalDtoResponse animalDtoResponse = new AnimalDtoResponse(animal);

        String serializedAnimalDto = objectMapper.writeValueAsString(animalDtoResponse);

        assertAll(() -> assertTrue(serializedAnimalDto.contains(String.format("\"id\":\"%s\"", animalId.toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"name\":\"%s\"", animal.getName()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"species\":\"%s\"", animal.getSpecies().toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"estimatedAge\":\"%s\"", animal.getEstimatedAge().toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"sex\":\"%s\"", animal.getSex().toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"smallPrimaryPictureUrl\":\"%s\"", smallPrimaryPictureUrl))));
    }

    @Test
    public void shouldSerializeAnimalDtoWithoutPrimaryLinkPicture() throws JsonProcessingException {
        UUID animalId = UUID.randomUUID();
        Animal animal = AnimalFactory.random().withIdentifier(animalId).build();
        AnimalDtoResponse animalDtoResponse = new AnimalDtoResponse(animal);

        String serializedAnimalDto = objectMapper.writeValueAsString(animalDtoResponse);

        assertAll(() -> assertTrue(serializedAnimalDto.contains(String.format("\"id\":\"%s\"", animalId.toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"name\":\"%s\"", animal.getName()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"species\":\"%s\"", animal.getSpecies().toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"estimatedAge\":\"%s\"", animal.getEstimatedAge().toString()))),
                  () -> assertTrue(serializedAnimalDto.contains(String.format("\"sex\":\"%s\"", animal.getSex().toString()))),
                  () -> assertFalse(serializedAnimalDto.contains("\"smallPrimaryPictureUrl\":")));
    }

    @Test
    public void shouldBeDeserializable() throws JSONException, JsonProcessingException {
        LinkPicture primaryLinkPicture = LinkPictureFactory.random().withPictureType(PictureType.PRIMARY).build();
        Animal animal = AnimalFactory.random().withPrimaryLinkPicture(primaryLinkPicture).build();
        AnimalDtoResponse expectedAnimalDtoResponse = new AnimalDtoResponse(animal);
        String animalDtoAsJson = new JSONObject()
                .put("id", animal.getIdentifier())
                .put("name", animal.getName())
                .put("species", animal.getSpecies().toString())
                .put("estimatedAge", animal.getEstimatedAge().toString())
                .put("sex", animal.getSex().toString())
                .put("smallPrimaryPictureUrl", primaryLinkPicture.getSmallImageUrl())
                .toString();

        AnimalDtoResponse animalDtoResponse = objectMapper.readValue(animalDtoAsJson, AnimalDtoResponse.class);

        Assertions.assertThat(animalDtoResponse).usingRecursiveComparison().isEqualTo(expectedAnimalDtoResponse);
    }
}