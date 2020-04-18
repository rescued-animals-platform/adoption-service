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

package ec.animal.adoption.repository.jpa;

import ec.animal.adoption.domain.organization.Organization;
import ec.animal.adoption.repository.jpa.model.JpaOrganization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static ec.animal.adoption.builders.AnimalBuilder.DEFAULT_ORGANIZATION_UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaOrganizationRepositoryIntegrationTest extends AbstractJpaRepositoryIntegrationTest {

    @Autowired
    private JpaOrganizationRepository jpaOrganizationRepository;

    @Test
    public void shouldFindJpaOrganizationByOrganizationUuid() {
        Optional<JpaOrganization> optionalJpaOrganization = jpaOrganizationRepository.findById(DEFAULT_ORGANIZATION_UUID);

        assertThat(optionalJpaOrganization.isPresent(), is(true));
        Organization organization = optionalJpaOrganization.get().toOrganization();
        assertAll(
                () -> assertEquals("My Test Organization", organization.getName()),
                () -> assertEquals("QUITO", organization.getCity()),
                () -> assertEquals("mytestorg@mail.com", organization.getEmail()),
                () -> assertEquals("Av. Orellana E23-54", organization.getReceptionAddress()),
                () -> assertEquals("http://www.mytestorg.com/adoption-form", organization.getAdoptionFormPdfUrl())
        );
    }
}
