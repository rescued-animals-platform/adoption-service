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

package ec.animal.adoption.domain.organization;

import ec.animal.adoption.domain.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        organizationService = new OrganizationService(organizationRepository);
    }

    @Test
    void shouldReturnOrganizationByItsIdentifier() {
        UUID organizationId = UUID.randomUUID();
        Organization expectedOrganization = OrganizationBuilder.random().withIdentifier(organizationId).build();
        when(organizationRepository.getBy(organizationId)).thenReturn(of(expectedOrganization));

        Organization actualOrganization = organizationService.getBy(organizationId);

        assertEquals(expectedOrganization, actualOrganization);
    }

    @Test
    void shouldThrowUnauthorizedException() {
        UUID organizationId = UUID.randomUUID();
        when(organizationRepository.getBy(organizationId)).thenReturn(empty());

        assertThrows(UnauthorizedException.class, () -> {
            organizationService.getBy(organizationId);
        });
    }
}