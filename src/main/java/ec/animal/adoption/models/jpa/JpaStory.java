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

import ec.animal.adoption.domain.Story;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "story")
public class JpaStory implements Serializable {

    private transient static final long serialVersionUID = -242532859161428810L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    private LocalDateTime registrationDate;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_uuid", nullable = false)
    private JpaAnimal jpaAnimal;

    @NotNull
    private String text;

    private JpaStory() {
        // Required by jpa
    }

    public JpaStory(final Story story, final JpaAnimal jpaAnimal) {
        this();
        this.setUuid(story.getUuid());
        this.setRegistrationDate(story.getRegistrationDate());
        this.text = story.getText();
        this.jpaAnimal = jpaAnimal;
    }

    private void setUuid(final UUID uuid) {
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    }

    private void setRegistrationDate(final LocalDateTime registrationDate) {
        this.registrationDate = registrationDate == null ? LocalDateTime.now() : registrationDate;
    }

    public Story toStory() {
        return new Story(this.uuid, this.registrationDate, this.text);
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaStory jpaStory = (JpaStory) o;

        if (uuid != null ? !uuid.equals(jpaStory.uuid) : jpaStory.uuid != null) return false;
        if (registrationDate != null ? !registrationDate.equals(jpaStory.registrationDate) : jpaStory.registrationDate != null)
            return false;
        if (jpaAnimal != null ? !jpaAnimal.equals(jpaStory.jpaAnimal) : jpaStory.jpaAnimal != null) return false;
        return text != null ? text.equals(jpaStory.text) : jpaStory.text == null;
    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (jpaAnimal != null ? jpaAnimal.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
