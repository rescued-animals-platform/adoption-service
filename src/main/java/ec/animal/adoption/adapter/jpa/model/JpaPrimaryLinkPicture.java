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

package ec.animal.adoption.adapter.jpa.model;

import ec.animal.adoption.domain.model.media.LinkPicture;
import ec.animal.adoption.domain.model.media.MediaLink;
import ec.animal.adoption.domain.model.media.PictureType;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "primary_link_picture")
public class JpaPrimaryLinkPicture implements Serializable {

    @Serial
    private static final transient long serialVersionUID = -832433659194420810L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private JpaAnimal jpaAnimal;

    private LocalDateTime registrationDate;

    @NotNull
    private String name;

    private String largeImagePublicId;
    private String largeImageUrl;
    private String smallImagePublicId;
    private String smallImageUrl;

    private JpaPrimaryLinkPicture() {
        // Required by jpa
    }

    public JpaPrimaryLinkPicture(final LinkPicture linkPicture, final JpaAnimal jpaAnimal) {
        this();
        this.setId(linkPicture.getIdentifier());
        this.jpaAnimal = jpaAnimal;
        this.setRegistrationDate(linkPicture.getRegistrationDate());
        this.name = linkPicture.getName();
        this.largeImagePublicId = linkPicture.getLargeImagePublicId();
        this.largeImageUrl = linkPicture.getLargeImageUrl();
        this.smallImagePublicId = linkPicture.getSmallImagePublicId();
        this.smallImageUrl = linkPicture.getSmallImageUrl();
    }

    private void setId(final UUID id) {
        this.id = id == null ? UUID.randomUUID() : id;
    }

    private void setRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate == null ? LocalDateTime.now() : registrationDate;
    }

    public LinkPicture toLinkPicture() {
        return new LinkPicture(
                this.id,
                this.registrationDate,
                this.name,
                PictureType.PRIMARY,
                new MediaLink(this.largeImagePublicId, this.largeImageUrl),
                new MediaLink(this.smallImagePublicId, this.smallImageUrl)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JpaPrimaryLinkPicture that = (JpaPrimaryLinkPicture) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
