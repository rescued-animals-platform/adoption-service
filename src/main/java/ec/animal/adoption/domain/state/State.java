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

package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.CaseFormat;
import org.reflections.Reflections;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "name")
@JsonSubTypes({@JsonSubTypes.Type(value = LookingForHuman.class),
               @JsonSubTypes.Type(value = Adopted.class),
               @JsonSubTypes.Type(value = Unavailable.class)})
public abstract class State implements Serializable {

    private transient static final long serialVersionUID = -312436659134428610L;

    @JsonProperty("date")
    private final LocalDateTime date;

    protected State(final LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @JsonIgnore
    public String getName() {
        return convertUpperCamelToLowerCamelCase(this.getClass().getSimpleName());
    }

    public static boolean isStateNameValid(final String stateName) {
        Reflections reflections = new Reflections(State.class.getPackageName());
        Set<Class<? extends State>> states = reflections.getSubTypesOf(State.class);

        return states.stream().map(Class::getSimpleName)
                     .map(State::convertUpperCamelToLowerCamelCase)
                     .anyMatch(n -> n.equals(stateName));
    }

    private static String convertUpperCamelToLowerCamelCase(final String value) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, value);
    }
}
