package ec.animal.adoption.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private final String message;

    public EntityNotFoundException() {
        this.message = "Unable to find the resource";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
