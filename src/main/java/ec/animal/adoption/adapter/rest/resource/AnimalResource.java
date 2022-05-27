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

package ec.animal.adoption.adapter.rest.resource;

import ec.animal.adoption.adapter.rest.jwt.AdminTokenUtils;
import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateRequest;
import ec.animal.adoption.adapter.rest.model.animal.AnimalCreateUpdateResponse;
import ec.animal.adoption.adapter.rest.model.animal.AnimalResponse;
import ec.animal.adoption.adapter.rest.model.animal.dto.AnimalDtoResponse;
import ec.animal.adoption.application.AnimalService;
import ec.animal.adoption.application.OrganizationService;
import ec.animal.adoption.domain.model.animal.Animal;
import ec.animal.adoption.domain.model.animal.Species;
import ec.animal.adoption.domain.model.animal.dto.AnimalDto;
import ec.animal.adoption.domain.model.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.model.characteristics.Size;
import ec.animal.adoption.domain.model.organization.Organization;
import ec.animal.adoption.domain.model.state.StateName;
import ec.animal.adoption.domain.service.PagedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/admin/animals")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalCreateUpdateResponse create(@RequestBody @Valid final AnimalCreateUpdateRequest animalCreateUpdateRequest,
                                             @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        AnimalDto animalDto = animalCreateUpdateRequest.toDomainWith(organization);
        Animal animal = animalService.create(animalDto);

        return AnimalCreateUpdateResponse.from(animal);
    }

    @PutMapping("/admin/animals/{id}")
    public AnimalCreateUpdateResponse update(@PathVariable("id") final UUID animalId,
                                             @RequestBody @Valid final AnimalCreateUpdateRequest animalCreateUpdateRequest,
                                             @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        AnimalDto animalDto = animalCreateUpdateRequest.toDomainWith(organization);
        Animal animal = animalService.update(animalId, animalDto);

        return AnimalCreateUpdateResponse.from(animal);
    }

    @GetMapping("/admin/animals/{id}")
    public AnimalResponse get(@PathVariable("id") final UUID animalId,
                              @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        Animal animal = animalService.getBy(animalId, organization);

        return AnimalResponse.from(animal);
    }

    @GetMapping("/admin/animals")
    public PagedEntity<AnimalResponse> listAll(final Pageable pageable,
                                               @AuthenticationPrincipal final Jwt token) {
        UUID organizationId = adminTokenUtils.extractOrganizationIdFrom(token);
        Organization organization = organizationService.getBy(organizationId);
        PagedEntity<Animal> animals = animalService.listAllFor(organization, pageable);

        return animals.map(AnimalResponse::from);
    }

    @GetMapping("/animals")
    public PagedEntity<AnimalDtoResponse> listAllWithFilters(
            @RequestParam("state") final StateName stateName,
            @RequestParam("species") final Species species,
            @RequestParam("physicalActivity") final PhysicalActivity physicalActivity,
            @RequestParam("animalSize") final Size size,
            final Pageable pageable
    ) {
        return animalService.listAllWithFilters(stateName, species, physicalActivity, size, pageable)
                            .map(AnimalDtoResponse::new);
    }
}
