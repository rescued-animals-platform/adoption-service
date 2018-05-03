package ec.animal.adoption.models.rest;

import java.io.Serializable;
import java.util.Objects;

public class ValidationError implements Serializable {
    private String field;
    private String message;

    public ValidationError() {
        // Required for serialization
    }

    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, message);
    }
}
