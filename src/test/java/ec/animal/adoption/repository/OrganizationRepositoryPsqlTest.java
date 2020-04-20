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

import ec.animal.adoption.builders.OrganizationBuilder;
import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.domain.organization.OrganizationRepository;
import ec.animal.adoption.repository.jpa.JpaOrganizationRepository;
import ec.animal.adoption.repository.jpa.model.JpaOrganization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationRepositoryPsqlTest {

    @Mock
    private JpaOrganizationRepository jpaOrganizationRepository;

    private OrganizationRepositoryPsql organizationRepositoryPsql;

    @BeforeEach
    void setUp() {
        organizationRepositoryPsql = new OrganizationRepositoryPsql(jpaOrganizationRepository);
    }

    @Test
    public void shouldBeAnInstanceOfOrganizationRepository() {
        assertThat(organizationRepositoryPsql, is(instanceOf(OrganizationRepository.class)));
    }

    @Test
    public void shouldGetOrganizationByItsIdentifier() {
        Organization organization = OrganizationBuilder.random().build();
        UUID organizationUuid = organization.getOrganizationUuid();
        when(jpaOrganizationRepository.findById(organizationUuid)).thenReturn(of(new JpaOrganization(organization)));

        Optional<Organization> organizationFound = organizationRepositoryPsql.getBy(organizationUuid);

        assertTrue(organizationFound.isPresent());
        assertEquals(organization, organizationFound.get());
    }

    @Test
    public void shouldReturnEmptyOrganization() {
        UUID organizationUuid = UUID.randomUUID();
        when(jpaOrganizationRepository.findById(organizationUuid)).thenReturn(empty());
        Optional<Organization> organization = organizationRepositoryPsql.getBy(organizationUuid);

        assertTrue(organization.isEmpty());
    }
}