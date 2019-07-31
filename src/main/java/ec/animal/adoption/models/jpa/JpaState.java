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

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import ec.animal.adoption.domain.state.State;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "state")
@SuppressWarnings({"PMD.SingularField", "PMD.UnusedPrivateField"})
public class JpaState implements Serializable {

    private transient static final long serialVersionUID = -232452658164448810L;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @SuppressWarnings("PMD.ShortVariable")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "animal_uuid")
    private JpaAnimal jpaAnimal;

    @NotNull
    private String stateName;

    @NotNull
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private State state;

    protected JpaState() {
        // Required by Jpa
    }

    public JpaState(final State state, final JpaAnimal jpaAnimal) {
        this();
        this.id = UUID.randomUUID();
        this.jpaAnimal = jpaAnimal;
        this.stateName = state.getClass().getSimpleName();
        this.state = state;
    }

    public State toState() {
        return this.state;
    }
}
