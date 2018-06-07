package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.picture.Image;
import ec.animal.adoption.domain.picture.SupportedImageExtension;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ImageValidator implements ConstraintValidator<ValidImage, Image> {

    @Override
    public boolean isValid(Image image, ConstraintValidatorContext context) {
        return Arrays.stream(SupportedImageExtension.values()).anyMatch(
                supportedImageExtension -> {
                    byte[] imageBytes = image.getBytes();
                    byte[] startingBytesOfImage = Arrays.copyOf(
                            imageBytes, supportedImageExtension.getStartingBytes().length
                    );
                    return Arrays.equals(startingBytesOfImage, supportedImageExtension.getStartingBytes());
                }
        );
    }
}
