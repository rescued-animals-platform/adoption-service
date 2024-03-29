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

public class Organization {

    private final UUID organizationId;
    private final String name;
    private final String city;
    private final String email;
    private final String receptionAddress;
    private final String adoptionFormPdfUrl;

    public Organization(final UUID organizationId,
                        final String name,
                        final String city,
                        final String email,
                        final String receptionAddress,
                        final String adoptionFormPdfUrl) {
        this.organizationId = organizationId;
        this.name = name;
        this.city = city;
        this.email = email;
        this.receptionAddress = receptionAddress;
        this.adoptionFormPdfUrl = adoptionFormPdfUrl;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getReceptionAddress() {
        return receptionAddress;
    }

    public String getAdoptionFormPdfUrl() {
        return adoptionFormPdfUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Organization that = (Organization) o;

        return organizationId != null ? organizationId.equals(that.organizationId) : that.organizationId == null;
    }

    @Override
    public int hashCode() {
        return organizationId != null ? organizationId.hashCode() : 0;
    }
}
