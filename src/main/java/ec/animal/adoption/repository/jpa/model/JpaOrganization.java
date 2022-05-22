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
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity(name = "organization")
public class JpaOrganization implements Serializable {

    private static final transient long serialVersionUID = -579031381673428820L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    private String name;
    private String city;
    private String email;
    private String receptionAddress;
    private String adoptionFormPdfUrl;

    private JpaOrganization() {
        // Required by jpa
    }

    public JpaOrganization(final Organization organization) {
        this();
        this.id = organization.getOrganizationId();
        this.name = organization.getName();
        this.city = organization.getCity();
        this.email = organization.getEmail();
        this.receptionAddress = organization.getReceptionAddress();
        this.adoptionFormPdfUrl = organization.getAdoptionFormPdfUrl();
    }

    public Organization toOrganization() {
        return new Organization(this.id,
                                this.name,
                                this.city,
                                this.email,
                                this.receptionAddress,
                                this.adoptionFormPdfUrl);
    }
}
