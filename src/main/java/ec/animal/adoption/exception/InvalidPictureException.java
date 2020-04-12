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

package ec.animal.adoption.exception;

import ec.animal.adoption.domain.media.SupportedImageExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InvalidPictureException extends RuntimeException {

    private transient static final long serialVersionUID = -442435159749429820L;

    private static final String MESSAGE = "The image(s) could not be processed. Check they meet the minimum " +
            "requirements: Supported extensions: %s. Maximum size: 1MB. Only PRIMARY picture type is supported.";

    public InvalidPictureException() {
        super(formatMessage());
    }

    public InvalidPictureException(final Throwable throwable) {
        super(formatMessage(), throwable);
    }

    private static String formatMessage() {
        return String.format(MESSAGE, Arrays.stream(SupportedImageExtension.values())
                .map(SupportedImageExtension::getExtension)
                .collect(Collectors.joining(", ")));
    }
}
