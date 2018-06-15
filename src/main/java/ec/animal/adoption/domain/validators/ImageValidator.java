package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.media.Image;
import ec.animal.adoption.domain.media.SupportedImageExtension;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ImageValidator implements ConstraintValidator<ValidImage, Image> {

    private static final int ONE_MEGA_BYTE = 1048576;
    private static final String INVALID_MESSAGE = "The image provided doesn't meet one or more of the requirements. " +
            "Supported extensions: %s. Maximum size: 1MB";

    @Override
    public boolean isValid(Image image, ConstraintValidatorContext context) {
        boolean isValid = SupportedImageExtension.getMatchFor(image.getExtension(), image.getContent()).isPresent() &&
                image.getSizeInBytes() <= ONE_MEGA_BYTE;

        return isValid || customizeContextAndReturnInvalid(context);
    }

    private boolean customizeContextAndReturnInvalid(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                String.format(INVALID_MESSAGE, Arrays.stream(SupportedImageExtension.values())
                        .map(SupportedImageExtension::getExtension)
                        .collect(Collectors.joining(", ")))
        ).addPropertyNode("image").addConstraintViolation();

        return false;
    }
}
