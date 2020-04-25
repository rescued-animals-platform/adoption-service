package ec.animal.adoption.api.jwt;

import ec.animal.adoption.domain.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AdminTokenUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdminTokenUtils.class);

    private final String organizationIdClaimName;

    @Autowired
    public AdminTokenUtils(@Value("${auth0.custom-claims.organization-id}") final String organizationIdClaimName) {
        this.organizationIdClaimName = organizationIdClaimName;
    }

    public UUID extractOrganizationUuidFrom(final Jwt token) {
        String organizationUuid = token.getClaimAsString(organizationIdClaimName);
        try {
            return Optional.ofNullable(organizationUuid)
                           .map(UUID::fromString)
                           .orElseThrow(UnauthorizedException::new);
        } catch (IllegalArgumentException ex) {
            LOGGER.info("Error while parsing organization uuid", ex);
            throw new UnauthorizedException(ex);
        }
    }
}
