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

package ec.animal.adoption.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAudienceValidatorTest {

    @Test
    void shouldReturnValidatorResultWithNoErrorsWhenAudienceInTokenMatchAudienceInValidator() {
        String audience = randomAlphabetic(10);
        JwtAudienceValidator jwtAudienceValidator = new JwtAudienceValidator(audience);
        Jwt token = mock(Jwt.class);
        when(token.getAudience()).thenReturn(Collections.singletonList(audience));

        OAuth2TokenValidatorResult tokenValidatorResult = jwtAudienceValidator.validate(token);

        assertFalse(tokenValidatorResult.hasErrors());
    }

    @Test
    void shouldReturnValidatorResultWithErrorWhenAudienceInTokenIsDifferentThanAudienceInValidator() {
        JwtAudienceValidator jwtAudienceValidator = new JwtAudienceValidator(randomAlphabetic(10));
        Jwt token = mock(Jwt.class);
        when(token.getAudience()).thenReturn(Collections.singletonList(randomAlphabetic(10)));

        OAuth2TokenValidatorResult tokenValidatorResult = jwtAudienceValidator.validate(token);

        assertTrue(tokenValidatorResult.hasErrors());
    }
}