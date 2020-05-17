package ec.animal.adoption.domain.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IllegalUpdateExceptionTest {

    @Test
    void shouldReturnMessage() {
        UUID existingAnimalId = UUID.randomUUID();
        String clinicalRecord = randomAlphabetic(10);
        IllegalUpdateException illegalUpdateException = new IllegalUpdateException(existingAnimalId, clinicalRecord);

        assertNotNull(illegalUpdateException.getMessage());
        assertTrue(illegalUpdateException.getMessage().contains(existingAnimalId.toString()));
        assertTrue(illegalUpdateException.getMessage().contains(clinicalRecord));
    }
}