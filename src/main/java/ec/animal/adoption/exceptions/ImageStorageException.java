package ec.animal.adoption.exceptions;

public class ImageStorageException extends RuntimeException {

    private final String message;

    public ImageStorageException() {
        this.message = "The image could not be stored";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
