package ec.animal.adoption.validator;

import org.junit.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AudienceValidatorTest {

    @Test
    public void shouldReturnValidatorResultWithNoErrorsWhenAudienceInTokenMatchAudienceInValidator() {
        String audience = randomAlphabetic(10);
        AudienceValidator audienceValidator = new AudienceValidator(audience);
        Jwt token = mock(Jwt.class);
        when(token.getAudience()).thenReturn(Collections.singletonList(audience));

        OAuth2TokenValidatorResult tokenValidatorResult = audienceValidator.validate(token);

        assertFalse(tokenValidatorResult.hasErrors());
    }

    @Test
    public void shouldReturnValidatorResultWithErrorWhenAudienceInTokenIsDifferentThanAudienceInValidator() {
        AudienceValidator audienceValidator = new AudienceValidator(randomAlphabetic(10));
        Jwt token = mock(Jwt.class);
        when(token.getAudience()).thenReturn(Collections.singletonList(randomAlphabetic(10)));

        OAuth2TokenValidatorResult tokenValidatorResult = audienceValidator.validate(token);

        assertTrue(tokenValidatorResult.hasErrors());
    }
}