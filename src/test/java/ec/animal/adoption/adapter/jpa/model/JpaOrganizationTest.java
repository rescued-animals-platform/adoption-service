package ec.animal.adoption.adapter.jpa.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class JpaOrganizationTest {

    @Test
    void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(JpaOrganization.class).suppress(Warning.SURROGATE_KEY).verify();
    }
}