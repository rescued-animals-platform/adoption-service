package ec.animal.adoption.domain.validators;

import com.google.common.io.Files;
import ec.animal.adoption.domain.picture.SupportedImageExtension;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FilenameValidator implements ConstraintValidator<ValidFilename, String> {

    @Override
    public boolean isValid(String filename, ConstraintValidatorContext context) {
        return filename != null && !filename.isEmpty() && SupportedImageExtension.getSupportedExtensions().
                contains(Files.getFileExtension(filename));
    }
}
