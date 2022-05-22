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

package ec.animal.adoption.repository.jpa.model;

import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaOrganizationTest {

    @Test
    void shouldMapOrganizationAndJpaOrganizationCorrectly() {
        Organization expectedOrganization = OrganizationFactory.random().build();

        JpaOrganization jpaOrganization = new JpaOrganization(expectedOrganization);
        Organization actualOrganization = jpaOrganization.toOrganization();

        assertAll(
                () -> assertEquals(expectedOrganization.getOrganizationId(), actualOrganization.getOrganizationId()),
                () -> assertEquals(expectedOrganization.getName(), actualOrganization.getName()),
                () -> assertEquals(expectedOrganization.getCity(), actualOrganization.getCity()),
                () -> assertEquals(expectedOrganization.getEmail(), actualOrganization.getEmail()),
                () -> assertEquals(expectedOrganization.getReceptionAddress(), actualOrganization.getReceptionAddress()),
                () -> assertEquals(expectedOrganization.getAdoptionFormPdfUrl(), actualOrganization.getAdoptionFormPdfUrl())
        );
    }
}