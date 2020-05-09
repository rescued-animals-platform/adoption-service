package ec.animal.adoption.domain.utils;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslatorUtilsTest {

    @Test
    void shouldPrefixMessageCodeBeforeGettingIt() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        ReflectionTestUtils.setField(TranslatorUtils.class, "messageSource", messageSource);
        String prefix = randomAlphabetic(10);
        String messageCode = randomAlphabetic(10);

        String translatedMessage = TranslatorUtils.toLocale(prefix, messageCode);

        assertEquals(String.format("%s.%s", prefix, messageCode), translatedMessage);
    }
}