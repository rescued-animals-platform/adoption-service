package ec.animal.adoption.domain.state;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LookingForHuman.class, name = "lookingForHuman"),
        @JsonSubTypes.Type(value = Adopted.class, name = "adopted"),
        @JsonSubTypes.Type(value = Unavailable.class, name = "unavailable")
})
public interface State {
}
