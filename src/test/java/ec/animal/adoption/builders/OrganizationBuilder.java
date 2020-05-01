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

package ec.animal.adoption.builders;

import ec.animal.adoption.domain.organization.Organization;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class OrganizationBuilder {

    private UUID organizationId;
    private String name;
    private String city;
    private String receptionAddress;
    private String email;
    private String adoptionFormPdfUrl;

    public static OrganizationBuilder random() {
        OrganizationBuilder organizationBuilder = new OrganizationBuilder();
        organizationBuilder.organizationId = UUID.randomUUID();
        organizationBuilder.name = randomAlphabetic(10);
        organizationBuilder.city = randomAlphabetic(10);
        organizationBuilder.email = organizationBuilder.name + "@email.com";
        organizationBuilder.receptionAddress = randomAlphabetic(20);
        organizationBuilder.adoptionFormPdfUrl = randomAlphabetic(20);
        return organizationBuilder;
    }

    public OrganizationBuilder withIdentifier(final UUID organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public Organization build() {
        return new Organization(this.organizationId,
                                this.name,
                                this.city,
                                this.email,
                                this.receptionAddress,
                                this.adoptionFormPdfUrl);
    }
}
