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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "state")
public class JpaState {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID id;

    @Column(nullable = false)
    private String stateName;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private State state;


    private JpaState() {
        // required by jpa
    }

    public JpaState(State state) {
        this();
        this.id = UUID.randomUUID();
        this.stateName = state.getClass().getSimpleName();
        this.state = state;
    }

    public State toState() {
        return this.state;
    }

    public void setState(State state) {
        this.stateName = state.getClass().getSimpleName();
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaState jpaState = (JpaState) o;

        if (id != null ? !id.equals(jpaState.id) : jpaState.id != null) return false;
        if (stateName != null ? !stateName.equals(jpaState.stateName) : jpaState.stateName != null) return false;
        return state != null ? state.equals(jpaState.state) : jpaState.state == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (stateName != null ? stateName.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
