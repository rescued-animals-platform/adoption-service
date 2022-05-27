package ec.animal.adoption.adapter.rest.jwt;

import ec.animal.adoption.domain.model.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AdminTokenUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminTokenUtils.class);

    private final String organizationIdClaimName;

    @Autowired
    public AdminTokenUtils(@Value("${auth0.custom-claims.organization-id}") final String organizationIdClaimName) {
        this.organizationIdClaimName = organizationIdClaimName;
    }

    public UUID extractOrganizationIdFrom(final Jwt token) {
        String organizationId = token.getClaimAsString(organizationIdClaimName);
        try {
            if (organizationId == null) {
                LOGGER.info("Token doesn't have the organization_id claim");
                throw new UnauthorizedException();
            }

            return UUID.fromString(organizationId);
        } catch (IllegalArgumentException ex) {
            LOGGER.info("Organization id found in token claim is not a valid UUID");
            throw new UnauthorizedException(ex);
        }
    }
}
