package ec.animal.adoption.domain.characteristics.temperament;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Balance.class, name = "balance"),
        @JsonSubTypes.Type(value = Sociability.class, name = "sociability"),
        @JsonSubTypes.Type(value = Docility.class, name = "docility")
})
public interface Temperament {
}
