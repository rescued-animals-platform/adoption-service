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

import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.EstimatedAge;
import ec.animal.adoption.domain.animal.Sex;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.media.LinkPicture;

import java.util.UUID;

@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
public class AnimalDto {

    @JsonProperty("id")
    private final UUID animalId;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("species")
    private final Species species;

    @JsonProperty("estimatedAge")
    private final EstimatedAge estimatedAge;

    @JsonProperty("sex")
    private final Sex sex;

    @JsonProperty("smallPrimaryPictureUrl")
    private final String smallPrimaryPictureUrl;

    public AnimalDto(final Animal animal) {
        this.animalId = animal.getIdentifier();
        this.name = animal.getName();
        this.species = animal.getSpecies();
        this.estimatedAge = animal.getEstimatedAge();
        this.sex = animal.getSex();
        this.smallPrimaryPictureUrl = animal.getPrimaryLinkPicture()
                                            .map(LinkPicture::getSmallImageUrl)
                                            .orElse(null);
    }
}
