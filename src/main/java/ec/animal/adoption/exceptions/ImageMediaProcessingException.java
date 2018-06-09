package ec.animal.adoption.exceptions;

public class ImageMediaProcessingException extends Exception {

    private final String message;

    public ImageMediaProcessingException() {
        this.message = "The image could not be processed";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
