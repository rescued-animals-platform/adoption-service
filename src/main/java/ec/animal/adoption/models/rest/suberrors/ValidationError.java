package ec.animal.adoption.models.rest.suberrors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError implements ApiSubError {

    @JsonProperty("field")
    private final String field;

    @JsonProperty("message")
    private final String message;

    @JsonCreator
    public ValidationError(@JsonProperty("field") String field, @JsonProperty("message") String message) {
        this.field = field;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidationError that = (ValidationError) o;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
