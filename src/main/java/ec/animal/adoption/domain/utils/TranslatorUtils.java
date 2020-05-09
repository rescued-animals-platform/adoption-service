package ec.animal.adoption.domain.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@SuppressWarnings("PMD.AssignmentToNonFinalStatic")
@SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
public final class TranslatorUtils {

    private static ResourceBundleMessageSource messageSource;

    @Autowired
    public TranslatorUtils(final ResourceBundleMessageSource messageSource) {
        TranslatorUtils.messageSource = messageSource;
    }

    public static String toLocale(final String prefix, final String messageCode) {
        String messageCodePrefixed = String.format("%s.%s", prefix, messageCode);
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCodePrefixed, null, locale);
    }
}
