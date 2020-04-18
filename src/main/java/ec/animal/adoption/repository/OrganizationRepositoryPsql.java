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

package ec.animal.adoption.repository;

import ec.animal.adoption.domain.exception.UnauthorizedException;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationRepository;
import ec.animal.adoption.repository.jpa.JpaOrganizationRepository;
import ec.animal.adoption.repository.jpa.model.JpaOrganization;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class OrganizationRepositoryPsql implements OrganizationRepository {

    private final JpaOrganizationRepository jpaOrganizationRepository;

    public OrganizationRepositoryPsql(final JpaOrganizationRepository jpaOrganizationRepository) {
        this.jpaOrganizationRepository = jpaOrganizationRepository;
    }

    @Override
    public Organization getBy(final UUID uuid) {
        Optional<JpaOrganization> jpaOrganization = jpaOrganizationRepository.findById(uuid);
        return jpaOrganization.map(JpaOrganization::toOrganization).orElseThrow(UnauthorizedException::new);
    }
}
