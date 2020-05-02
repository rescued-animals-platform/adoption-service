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
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "name")
@JsonSubTypes({@JsonSubTypes.Type(value = LookingForHuman.class, name = "lookingForHuman"),
               @JsonSubTypes.Type(value = LookingForHuman.class, name = "LookingForHuman"),
               @JsonSubTypes.Type(value = LookingForHuman.class, name = "Looking for human"),
               @JsonSubTypes.Type(value = LookingForHuman.class, name = "looking for human"),
               @JsonSubTypes.Type(value = LookingForHuman.class, name = "LOOKING_FOR_HUMAN"),
               @JsonSubTypes.Type(value = LookingForHuman.class, name = "looking_for_human"),
               @JsonSubTypes.Type(value = Adopted.class, name = "adopted"),
               @JsonSubTypes.Type(value = Adopted.class, name = "Adopted"),
               @JsonSubTypes.Type(value = Adopted.class, name = "ADOPTED"),
               @JsonSubTypes.Type(value = Unavailable.class, name = "unavailable"),
               @JsonSubTypes.Type(value = Unavailable.class, name = "Unavailable"),
               @JsonSubTypes.Type(value = Unavailable.class, name = "UNAVAILABLE")})
public abstract class State implements Serializable {

    private transient static final long serialVersionUID = -312436659134428610L;
    private static final Set<String> VALID_LOOKING_FOR_HUMAN_STATE_NAMES = Set.of("lookingForHuman",
                                                                                  "LookingForHuman",
                                                                                  "Looking for human",
                                                                                  "looking for human",
                                                                                  "LOOKING_FOR_HUMAN",
                                                                                  "looking_for_human");
    private static final Set<String> VALID_ADOPTED_STATE_NAMES = Set.of("adopted",
                                                                        "Adopted",
                                                                        "ADOPTED");
    private static final Set<String> VALID_UNAVAILABLE_STATE_NAMES = Set.of("unavailable",
                                                                            "Unavailable",
                                                                            "UNAVAILABLE");

    @JsonProperty("date")
    private final LocalDateTime date;

    protected State(final LocalDateTime date) {
        this.date = date == null ? LocalDateTime.now() : date;
    }

    @JsonIgnore
    public LocalDateTime getRegistrationDate() {
        return date;
    }

    @JsonIgnore
    public String getName() {
        return convertUpperCamelToLowerCamelCase(this.getClass().getSimpleName());
    }

    public static boolean isStateNameValid(@NonNull final String stateName) {
        return Stream.of(
                VALID_LOOKING_FOR_HUMAN_STATE_NAMES,
                VALID_ADOPTED_STATE_NAMES,
                VALID_UNAVAILABLE_STATE_NAMES
        ).anyMatch(validStateNames -> validStateNames.contains(stateName));
    }

    public static String normalize(final String stateName) {
        if (stateName == null) {
            return null;
        }

        if (VALID_LOOKING_FOR_HUMAN_STATE_NAMES.contains(stateName)) {
            return convertUpperCamelToLowerCamelCase(LookingForHuman.class.getSimpleName());
        } else if (VALID_ADOPTED_STATE_NAMES.contains(stateName)) {
            return convertUpperCamelToLowerCamelCase(Adopted.class.getSimpleName());
        } else if (VALID_UNAVAILABLE_STATE_NAMES.contains(stateName)) {
            return convertUpperCamelToLowerCamelCase(Unavailable.class.getSimpleName());
        } else {
            return null;
        }
    }

    private static String convertUpperCamelToLowerCamelCase(final String value) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, value);
    }
}
