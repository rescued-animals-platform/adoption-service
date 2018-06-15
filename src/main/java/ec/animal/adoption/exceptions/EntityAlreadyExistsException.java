package ec.animal.adoption.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {

    private final String message;

    public EntityAlreadyExistsException() {
        this.message = "The resource already exists";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
