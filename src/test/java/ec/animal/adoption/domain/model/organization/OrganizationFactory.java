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

package ec.animal.adoption.domain.model.organization;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class OrganizationFactory {

    public static final UUID DEFAULT_ORGANIZATION_ID = UUID.fromString("56009119-44bd-469a-a59b-401ab23d19ca");
    public static final UUID ANOTHER_ORGANIZATION_ID = UUID.fromString("dd20a44f-8aea-49bb-bfc7-cb57f6a4cd6d");

    private UUID organizationId;
    private String name;
    private String city;
    private String receptionAddress;
    private String email;
    private String adoptionFormPdfUrl;

    public static OrganizationFactory random() {
        OrganizationFactory organizationFactory = new OrganizationFactory();
        organizationFactory.organizationId = UUID.randomUUID();
        organizationFactory.name = randomAlphabetic(10);
        organizationFactory.city = randomAlphabetic(10);
        organizationFactory.email = organizationFactory.name + "@email.com";
        organizationFactory.receptionAddress = randomAlphabetic(20);
        organizationFactory.adoptionFormPdfUrl = randomAlphabetic(20);
        return organizationFactory;
    }

    public static OrganizationFactory randomDefaultOrganization() {
        OrganizationFactory organizationFactory = random();
        organizationFactory.organizationId = DEFAULT_ORGANIZATION_ID;
        return organizationFactory;
    }

    public static OrganizationFactory randomAnotherOrganization() {
        OrganizationFactory organizationFactory = random();
        organizationFactory.organizationId = ANOTHER_ORGANIZATION_ID;
        return organizationFactory;
    }

    public OrganizationFactory withIdentifier(final UUID organizationId) {
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
