package ec.animal.adoption.domain.media;

import java.util.Arrays;
import java.util.Optional;

public enum SupportedImageExtension {
    JPEG(new byte[]{(byte) 0xff, (byte) 0xd8}, "jpeg"),
    JPG(new byte[]{(byte) 0xff, (byte) 0xd8}, "jpg"),
    PNG(new byte[]{
            (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a
    }, "png");

    private final String extension;
    private final byte[] startingBytes;

    SupportedImageExtension(byte[] startingBytes, String extension) {
        this.extension = extension;
        this.startingBytes = startingBytes;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getStartingBytes() {
        return startingBytes;
    }

    public static Optional<SupportedImageExtension> getMatchFor(String extension, byte[] content) {
        return Arrays.stream(SupportedImageExtension.values()).filter(s -> {
            byte[] startingBytesFromContent = Arrays.copyOf(content, s.startingBytes.length);
            return s.extension.equals(extension) && Arrays.equals(startingBytesFromContent, s.startingBytes);
        }).findFirst();
    }
}
