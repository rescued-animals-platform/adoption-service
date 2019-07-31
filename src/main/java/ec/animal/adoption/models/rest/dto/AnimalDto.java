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

package ec.animal.adoption.models.rest.dto;

import ec.animal.adoption.domain.EstimatedAge;
import ec.animal.adoption.domain.Sex;
import ec.animal.adoption.domain.Species;

import java.util.UUID;

@SuppressWarnings("PMD")
public class AnimalDto {

    private final UUID animalUuid;
    private final String name;
    private final Species species;
    private final EstimatedAge estimatedAge;
    private final Sex sex;

    public AnimalDto(
            final UUID animalUuid,
            final String name,
            final Species species,
            final EstimatedAge estimatedAge,
            final Sex sex
    ) {
        this.animalUuid = animalUuid;
        this.name = name;
        this.species = species;
        this.estimatedAge = estimatedAge;
        this.sex = sex;
    }
}
