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

package ec.animal.adoption.adapter.rest.model.animal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.adapter.rest.model.characteristics.CharacteristicsResponse;
import ec.animal.adoption.adapter.rest.model.media.LinkPictureResponse;
import ec.animal.adoption.adapter.rest.model.state.StateResponse;
import ec.animal.adoption.adapter.rest.model.story.StoryResponse;
import ec.animal.adoption.adapter.rest.service.LinkPictureResponseMapper;
import ec.animal.adoption.adapter.rest.service.StateResponseMapper;
import ec.animal.adoption.domain.model.animal.Animal;

import java.time.LocalDateTime;
import java.util.UUID;

public class AnimalResponse {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("registrationDate")
    private final LocalDateTime registrationDate;

    @JsonProperty("clinicalRecord")
    private final String clinicalRecord;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("species")
    private final String species;

    @JsonProperty("estimatedAge")
    private final String estimatedAge;

    @JsonProperty("sex")
    private final String sex;

    @JsonProperty("state")
    private final StateResponse state;

    @JsonProperty(value = "primaryLinkPicture")
    private final LinkPictureResponse primaryLinkPicture;

    @JsonProperty(value = "characteristics")
    private final CharacteristicsResponse characteristics;

    @JsonProperty(value = "story")
    private final StoryResponse story;

    @JsonCreator
    private AnimalResponse(@JsonProperty("id") final UUID id,
                           @JsonProperty("registrationDate") final LocalDateTime registrationDate,
                           @JsonProperty("clinicalRecord") final String clinicalRecord,
                           @JsonProperty("name") final String name,
                           @JsonProperty("species") final String species,
                           @JsonProperty("estimatedAge") final String estimatedAge,
                           @JsonProperty("sex") final String sex,
                           @JsonProperty("state") final StateResponse state,
                           @JsonProperty(value = "primaryLinkPicture") final LinkPictureResponse primaryLinkPicture,
                           @JsonProperty(value = "characteristics") final CharacteristicsResponse characteristics,
                           @JsonProperty(value = "story") final StoryResponse story) {
        this.id = id;
        this.registrationDate = registrationDate;
        this.clinicalRecord = clinicalRecord;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
        this.state = state;
        this.primaryLinkPicture = primaryLinkPicture;
        this.characteristics = characteristics;
        this.story = story;
    }

    public static AnimalResponse from(final Animal animal) {
        return new AnimalResponse(animal.getIdentifier(),
                                  animal.getRegistrationDate(),
                                  animal.getClinicalRecord(),
                                  animal.getName(),
                                  animal.getSpecies().name(),
                                  animal.getEstimatedAge().name(),
                                  animal.getSex().name(),
                                  StateResponseMapper.MAPPER.toStateResponse(animal.getState()),
                                  animal.getPrimaryLinkPicture().map(LinkPictureResponseMapper.MAPPER::toLinkPictureResponse).orElse(null),
                                  animal.getCharacteristics().map(CharacteristicsResponse::from).orElse(null),
                                  animal.getStory().map(StoryResponse::from).orElse(null));
    }

    public UUID getId() {
        return id;
    }
}
