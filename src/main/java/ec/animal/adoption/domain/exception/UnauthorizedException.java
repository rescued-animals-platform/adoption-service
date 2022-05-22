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

package ec.animal.adoption.domain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnauthorizedException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedException.class);
    private static final String UNAUTHORIZED = "Unauthorized";
    private static final transient long serialVersionUID = -232426153813421923L;

    public UnauthorizedException(String contextualInfo) {
        super(UNAUTHORIZED);
        LOGGER.info(contextualInfo);
    }

    public UnauthorizedException(String contextualInfo, final Throwable cause) {
        super(UNAUTHORIZED, cause);
        LOGGER.info(contextualInfo);
    }
}
