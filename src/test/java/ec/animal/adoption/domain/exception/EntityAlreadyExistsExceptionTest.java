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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class EntityAlreadyExistsExceptionTest {

    private static final String EXPECTED_MESSAGE = "The resource already exists";

    @Test
    public void shouldReturnMessageAndSetCause() {
        Throwable expectedCause = mock(Throwable.class);

        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException(expectedCause);

        assertEquals(EXPECTED_MESSAGE, entityAlreadyExistsException.getMessage());
        assertEquals(expectedCause, entityAlreadyExistsException.getCause());
    }

    @Test
    public void shouldReturnMessage() {
        EntityAlreadyExistsException entityAlreadyExistsException = new EntityAlreadyExistsException();

        assertEquals(EXPECTED_MESSAGE, entityAlreadyExistsException.getMessage());
    }
}