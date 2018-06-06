package ec.animal.adoption.exceptions;

public class ImageProcessingException extends Exception {

    private final String message;

    public ImageProcessingException() {
        this.message = "The image could not be processed";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
