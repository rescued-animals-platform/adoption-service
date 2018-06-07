package ec.animal.adoption.domain.picture;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SupportedImageExtension {
    JPEG(new byte[]{(byte) 0xff, (byte) 0xd8}, "jpeg", "jpg"),
    PNG(new byte[]{
            (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a
    }, "png");

    private final List<String> extensions;
    private final byte[] startingBytes;

    SupportedImageExtension(byte[] startingBytes, String ... extensions) {
        this.extensions = Arrays.asList(extensions);
        this.startingBytes = startingBytes;
    }

    public static List<String> getSupportedExtensions() {
        return Arrays.stream(values()).flatMap(s -> s.extensions.stream()).collect(Collectors.toList());
    }

    public byte[] getStartingBytes() {
        return startingBytes;
    }
}
