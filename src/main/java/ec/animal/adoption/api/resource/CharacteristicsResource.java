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

package ec.animal.adoption.api.resource;

import ec.animal.adoption.domain.characteristics.Characteristics;
import ec.animal.adoption.domain.characteristics.CharacteristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/animals/{animalUuid}/characteristics")
public class CharacteristicsResource {

    private final CharacteristicsService characteristicsService;

    @Autowired
    public CharacteristicsResource(final CharacteristicsService characteristicsService) {
        this.characteristicsService = characteristicsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Characteristics create(
            @PathVariable("animalUuid") final UUID animalUuid,
            @RequestBody @Valid final Characteristics characteristics
    ) {
        return characteristicsService.create(animalUuid, characteristics);
    }

    @GetMapping
    public Characteristics get(@PathVariable("animalUuid") final UUID animalUuid) {
        return characteristicsService.getBy(animalUuid);
    }
}
