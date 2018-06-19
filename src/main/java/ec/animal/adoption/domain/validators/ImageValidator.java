/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

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
