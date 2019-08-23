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

package ec.animal.adoption.models.jpa;

import ec.animal.adoption.domain.media.LinkPicture;
import ec.animal.adoption.domain.media.MediaLink;
import ec.animal.adoption.domain.media.PictureType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "primary_link_picture")
public class JpaPrimaryLinkPicture implements Serializable {

    private transient static final long serialVersionUID = -832433659194420810L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_uuid", nullable = false)
    private JpaAnimal jpaAnimal;

    private LocalDateTime registrationDate;

    @NotNull
    private String name;

    private String largeImageUrl;

    private String smallImageUrl;

    private JpaPrimaryLinkPicture() {
        // Required by jpa
    }

    public JpaPrimaryLinkPicture(final LinkPicture linkPicture, final JpaAnimal jpaAnimal) {
        this();
        this.setUuid(linkPicture.getUuid());
        this.jpaAnimal = jpaAnimal;
        this.setRegistrationDate(linkPicture.getRegistrationDate());
        this.name = linkPicture.getName();
        this.largeImageUrl = linkPicture.getLargeImageUrl();
        this.smallImageUrl = linkPicture.getSmallImageUrl();
    }

    private void setUuid(final UUID uuid) {
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    }

    private void setRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate == null ? LocalDateTime.now() : registrationDate;
    }

    public LinkPicture toLinkPicture() {
        return new LinkPicture(
                this.uuid,
                this.registrationDate,
                this.name,
                PictureType.PRIMARY,
                new MediaLink(this.largeImageUrl),
                new MediaLink(this.smallImageUrl)
        );
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaPrimaryLinkPicture that = (JpaPrimaryLinkPicture) o;

        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (jpaAnimal != null ? !jpaAnimal.equals(that.jpaAnimal) : that.jpaAnimal != null) return false;
        if (registrationDate != null ? !registrationDate.equals(that.registrationDate) : that.registrationDate != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (largeImageUrl != null ? !largeImageUrl.equals(that.largeImageUrl) : that.largeImageUrl != null)
            return false;
        return smallImageUrl != null ? smallImageUrl.equals(that.smallImageUrl) : that.smallImageUrl == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (jpaAnimal != null ? jpaAnimal.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (largeImageUrl != null ? largeImageUrl.hashCode() : 0);
        result = 31 * result + (smallImageUrl != null ? smallImageUrl.hashCode() : 0);
        return result;
    }
}
