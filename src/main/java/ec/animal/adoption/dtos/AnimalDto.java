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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Species;

import java.util.UUID;

@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
public class AnimalDto {

    @JsonProperty("animalUuid")
    private final UUID animalUuid;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("species")
    private final Species species;

    @JsonProperty("estimatedAge")
    private final EstimatedAge estimatedAge;

    @JsonProperty("sex")
    private final Sex sex;

    @JsonCreator
    public AnimalDto(
            @JsonProperty("animalUuid") final UUID animalUuid,
            @JsonProperty("name") final String name,
            @JsonProperty("species") final Species species,
            @JsonProperty("estimatedAge") final EstimatedAge estimatedAge,
            @JsonProperty("sex") final Sex sex
    ) {
        this.animalUuid = animalUuid;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
    }

    public UUID getAnimalUuid() {
        return animalUuid;
    }
}
