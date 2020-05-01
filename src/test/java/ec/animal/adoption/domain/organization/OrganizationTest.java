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

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrganizationTest {

    @Test
    void shouldCreateOrganization() {
        UUID organizationId = UUID.randomUUID();
        String name = randomAlphabetic(10);
        String city = randomAlphabetic(10);
        String email = randomAlphabetic(10);
        String receptionAddress = randomAlphabetic(20);
        String adoptionFormPdfUrl = randomAlphabetic(20);

        Organization organization = new Organization(organizationId, name, city, email, receptionAddress, adoptionFormPdfUrl);

        assertAll(() -> assertEquals(name, organization.getName()),
                  () -> assertEquals(city, organization.getCity()),
                  () -> assertEquals(email, organization.getEmail()),
                  () -> assertEquals(receptionAddress, organization.getReceptionAddress()),
                  () -> assertEquals(adoptionFormPdfUrl, organization.getAdoptionFormPdfUrl()));
    }
}