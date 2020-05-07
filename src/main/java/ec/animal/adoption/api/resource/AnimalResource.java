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

import ec.animal.adoption.api.jwt.AdminTokenUtils;
import ec.animal.adoption.api.model.animal.CreateAnimalRequest;
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalService;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.animal.dto.AnimalDto;
import ec.animal.adoption.domain.animal.dto.CreateAnimalDto;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationService;
import ec.animal.adoption.domain.state.StateName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@Validated
@RestController
public class AnimalResource {

    private final AnimalService animalService;
    private final OrganizationService organizationService;
    private final AdminTokenUtils adminTokenUtils;

    @Autowired
    public AnimalResource(final AnimalService animalService,
                          final OrganizationService organizationService,
                          final AdminTokenUtils adminTokenUtils) {
        this.animalService = animalService;
        this.organizationService = organizationService;
        this.adminTokenUtils = adminTokenUtils;
    }

    @PostMapping(value = "/admin/animals", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Animal create(@RequestBody @Valid final CreateAnimalRequest createAnimalRequest,
                         @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        CreateAnimalDto createAnimalDto = createAnimalRequest.toDomainWith(organization);
        return animalService.create(createAnimalDto);
    }

    @GetMapping("/admin/animals/{id}")
    public Animal get(@PathVariable("id") final UUID animalId,
                      @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        return animalService.getBy(animalId, organization);
    }

    @GetMapping("/admin/animals")
    public PagedEntity<Animal> listAll(final Pageable pageable,
                                       @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        return animalService.listAllFor(organization, pageable);
    }

    @GetMapping("/animals")
    public PagedEntity<AnimalDto> listAllWithFilters(
            @RequestParam("state") final StateName stateName,
            @RequestParam("species") final Species species,
            @RequestParam("physicalActivity") final PhysicalActivity physicalActivity,
            @RequestParam("animalSize") final Size size,
            final Pageable pageable
    ) {
        return animalService.listAllWithFilters(stateName, species, physicalActivity, size, pageable);
    }
}
