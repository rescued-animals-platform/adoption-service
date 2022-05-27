package ec.animal.adoption.adapter.rest.jwt;

import ec.animal.adoption.domain.model.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminTokenUtilsTest {

    @Mock
    private Jwt token;

    private String organizationIdClaimName;
    private AdminTokenUtils adminTokenUtils;

    @BeforeEach
    void setUp() {
        organizationIdClaimName = randomAlphabetic(10);
        adminTokenUtils = new AdminTokenUtils(organizationIdClaimName);
    }

    @Test
    void shouldReturnOrganizationIdFromTokenClaim() {
        UUID expectedOrganizationId = UUID.randomUUID();
        when(token.getClaimAsString(organizationIdClaimName)).thenReturn(expectedOrganizationId.toString());

        UUID actualOrganizationId = adminTokenUtils.extractOrganizationIdFrom(token);

        assertEquals(expectedOrganizationId, actualOrganizationId);
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenOrganizationIdFromClaimIsNull() {
        when(token.getClaimAsString(organizationIdClaimName)).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> adminTokenUtils.extractOrganizationIdFrom(token));
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenOrganizationIdFromClaimIsNotAValidUUID() {
        when(token.getClaimAsString(organizationIdClaimName)).thenReturn(randomAlphabetic(10));

        assertThrows(UnauthorizedException.class, () -> adminTokenUtils.extractOrganizationIdFrom(token));
    }
}