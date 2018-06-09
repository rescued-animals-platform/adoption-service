package ec.animal.adoption.domain.validators;

import ec.animal.adoption.domain.media.ImageMedia;
import ec.animal.adoption.domain.media.SupportedImageExtension;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ImageMediaValidator implements ConstraintValidator<ValidImageMedia, ImageMedia> {

    @Override
    public boolean isValid(ImageMedia imageMedia, ConstraintValidatorContext context) {
        return SupportedImageExtension.getMatchFor(imageMedia.getExtension(), imageMedia.getContent()).isPresent() ||
                customizeContextAndReturnInvalid(context);
    }

    private boolean customizeContextAndReturnInvalid(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Unsupported file extension. Supported extensions are: ".concat(
                        Arrays.stream(SupportedImageExtension.values()).map(SupportedImageExtension::getExtension).
                                collect(Collectors.joining(", "))
                )
        ).addPropertyNode("image").addConstraintViolation();

        return false;
    }
}
