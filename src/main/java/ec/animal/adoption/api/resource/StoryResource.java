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
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationService;
import ec.animal.adoption.domain.story.Story;
import ec.animal.adoption.domain.story.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class StoryResource {

    private final StoryService storyService;
    private final OrganizationService organizationService;
    private final AdminTokenUtils adminTokenUtils;

    @Autowired
    public StoryResource(final StoryService storyService,
                         final OrganizationService organizationService,
                         final AdminTokenUtils adminTokenUtils) {
        this.storyService = storyService;
        this.organizationService = organizationService;
        this.adminTokenUtils = adminTokenUtils;
    }

    @PostMapping("/admin/animals/{animalUuid}/story")
    @ResponseStatus(HttpStatus.CREATED)
    public Story create(@PathVariable("animalUuid") final UUID animalUuid,
                        @RequestBody @Valid final Story story,
                        @AuthenticationPrincipal final Jwt token) {
        UUID organizationUuid = adminTokenUtils.extractOrganizationUuidFrom(token);
        Organization organization = organizationService.getBy(organizationUuid);
        return storyService.createFor(animalUuid, organization, story);
    }

    @PutMapping("/admin/animals/{animalUuid}/story")
    public Story update(@PathVariable("animalUuid") final UUID animalUuid,
                        @RequestBody @Valid final Story story,
                        @AuthenticationPrincipal final Jwt token) {
        UUID organizationUuid = adminTokenUtils.extractOrganizationUuidFrom(token);
        Organization organization = organizationService.getBy(organizationUuid);
        return storyService.updateFor(animalUuid, organization, story);
    }

    @GetMapping("/animals/{animalUuid}/story")
    public Story get(@PathVariable("animalUuid") final UUID animalUuid) {
        return storyService.getBy(animalUuid);
    }
}
