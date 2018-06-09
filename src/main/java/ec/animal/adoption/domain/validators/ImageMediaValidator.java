package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.SupportedImageExtension;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ImageMediaValidator implements ConstraintValidator<ValidImageMedia, ImageMedia> {

    private static final int ONE_MEGA_BYTE = 1048576;
    private static final String INVALID_MESSAGE = "The image provided doesn't meet one or more of the requirements. " +
            "Supported extensions: %s. Maximum size: 1MB";

    @Override
    public boolean isValid(ImageMedia imageMedia, ConstraintValidatorContext context) {
        boolean isValid = SupportedImageExtension.getMatchFor(
                imageMedia.getExtension(), imageMedia.getContent()
        ).isPresent() && imageMedia.getSizeInBytes() <= ONE_MEGA_BYTE;

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
