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
import ec.animal.adoption.domain.PagedEntity;
import ec.animal.adoption.domain.animal.Animal;
import ec.animal.adoption.domain.animal.AnimalService;
import ec.animal.adoption.domain.animal.Species;
import ec.animal.adoption.domain.animal.dto.AnimalDto;
import ec.animal.adoption.domain.characteristics.PhysicalActivity;
import ec.animal.adoption.domain.characteristics.Size;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

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
    public Animal create(@RequestBody @Valid final Animal animal,
                         @AuthenticationPrincipal final Jwt token) {
        UUID organizationUuid = adminTokenUtils.extractOrganizationUuidFrom(token);
        Organization organization = organizationService.getBy(organizationUuid);
        animal.setOrganization(organization);
        return animalService.create(animal);
    }

    @GetMapping("/admin/animals/{uuid}")
    public Animal get(@PathVariable("uuid") final UUID animalUuid,
                      @AuthenticationPrincipal final Jwt token) {
        UUID organizationUuid = adminTokenUtils.extractOrganizationUuidFrom(token);
        Organization organization = organizationService.getBy(organizationUuid);
        return animalService.getBy(animalUuid, organization);
    }

    @GetMapping("/admin/animals")
    public PagedEntity<Animal> listAll(final Pageable pageable,
                                       @AuthenticationPrincipal final Jwt token) {
        UUID organizationUuid = adminTokenUtils.extractOrganizationUuidFrom(token);
        Organization organization = organizationService.getBy(organizationUuid);
        return animalService.listAllFor(organization, pageable);
    }

    @GetMapping("/animals")
    public PagedEntity<AnimalDto> listAllWithFilters(
            @RequestParam("state") final String stateName,
            @RequestParam("species") final Species species,
            @RequestParam("physicalActivity") final PhysicalActivity physicalActivity,
            @RequestParam("animalSize") final Size size,
            final Pageable pageable
    ) {
        return animalService.listAllWithFilters(stateName, species, physicalActivity, size, pageable);
    }
}
