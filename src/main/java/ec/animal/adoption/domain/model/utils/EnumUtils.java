package ec.animal.adoption.domain.model.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class EnumUtils {

    private EnumUtils() {
        // Utility class
    }

    public static Function<Enum<?>[], Optional<Enum<?>>> forValue(final String value) {
        return enums -> {
            String normalizedValue = StringUtils.trimToEmpty(value);
            return Stream.of(enums)
                         .filter(anEnum -> anEnum.name().equals(normalizedValue))
                         .findFirst();
        };
    }
}
