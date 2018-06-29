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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.animal.adoption.domain.state.Adopted;
import ec.animal.adoption.domain.state.LookingForHuman;
import ec.animal.adoption.domain.state.State;
import ec.animal.adoption.domain.state.Unavailable;
import ec.animal.adoption.exceptions.UnexpectedException;
import ec.animal.adoption.helpers.JsonHelper;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.UUID;

@Entity(name = "state")
public class JpaState {

    private static ObjectMapper objectMapper = JsonHelper.getObjectMapper();

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "animal_uuid")
    private JpaAnimal jpaAnimal;

    @NotNull
    private String stateName;

    @Column(name = "state")
    private String stateAsJson;

    protected JpaState() {
        // Required by Jpa
    }

    public static JpaState getFor(State state, JpaAnimal jpaAnimal) {
        JpaState jpaState = new JpaState();
        jpaState.id = UUID.randomUUID();
        jpaState.jpaAnimal = jpaAnimal;
        jpaState.stateName = state.getClass().getSimpleName();
        JpaStateable jpaStateable = getJpaStateable(state);
        try {
            jpaState.stateAsJson = objectMapper.writeValueAsString(jpaStateable);
        } catch (JsonProcessingException e) {
            throw new UnexpectedException(e);
        }
        return jpaState;
    }

    private static JpaStateable getJpaStateable(State state) {
        if(state instanceof LookingForHuman) {
            return new JpaLookingForHuman((LookingForHuman) state);
        } else if(state instanceof Adopted) {
            return new JpaAdopted((Adopted) state);
        } else if(state instanceof Unavailable) {
            return new JpaUnavailable((Unavailable) state);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public State toState() {
        try {
            JpaStateable jpaStateable = objectMapper.readValue(this.stateAsJson, JpaStateable.class);
            return jpaStateable.toState();
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaState jpaState = (JpaState) o;

        if (id != null ? !id.equals(jpaState.id) : jpaState.id != null) return false;
        if (jpaAnimal != null ? !jpaAnimal.equals(jpaState.jpaAnimal) : jpaState.jpaAnimal != null) return false;
        if (stateName != null ? !stateName.equals(jpaState.stateName) : jpaState.stateName != null) return false;
        return stateAsJson != null ? stateAsJson.equals(jpaState.stateAsJson) : jpaState.stateAsJson == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (jpaAnimal != null ? jpaAnimal.hashCode() : 0);
        result = 31 * result + (stateName != null ? stateName.hashCode() : 0);
        result = 31 * result + (stateAsJson != null ? stateAsJson.hashCode() : 0);
        return result;
    }
}
