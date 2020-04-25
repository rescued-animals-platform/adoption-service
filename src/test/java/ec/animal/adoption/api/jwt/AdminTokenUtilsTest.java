package ec.animal.adoption.api.jwt;

import ec.animal.adoption.domain.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
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
    void shouldReturnOrganizationUuidFromTokenClaim() {
        UUID expectedOrganizationUuid = UUID.randomUUID();
        when(token.getClaimAsString(organizationIdClaimName)).thenReturn(expectedOrganizationUuid.toString());

        UUID actualOrganizationUuid = adminTokenUtils.extractOrganizationUuidFrom(token);

        assertEquals(expectedOrganizationUuid, actualOrganizationUuid);
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenOrganizationIdFromClaimIsNull() {
        when(token.getClaimAsString(organizationIdClaimName)).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> {
            adminTokenUtils.extractOrganizationUuidFrom(token);
        });
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenOrganizationIdFromClaimIsNotAValidUuid() {
        when(token.getClaimAsString(organizationIdClaimName)).thenReturn(randomAlphabetic(10));

        assertThrows(UnauthorizedException.class, () -> {
            adminTokenUtils.extractOrganizationUuidFrom(token);
        });
    }
}