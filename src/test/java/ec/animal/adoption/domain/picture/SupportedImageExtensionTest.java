package ec.animal.adoption.domain.picture;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SupportedImageExtensionTest {

    @Test
    public void shouldSupportJpeg() {
        List<String> supportedExtensions = SupportedImageExtension.getSupportedExtensions();

        assertTrue(supportedExtensions.contains("jpeg"));
    }

    @Test
    public void shouldSupportJpg() {
        List<String> supportedExtensions = SupportedImageExtension.getSupportedExtensions();

        assertTrue(supportedExtensions.contains("jpg"));
    }

    @Test
    public void shouldSupportPng() {
        List<String> supportedExtensions = SupportedImageExtension.getSupportedExtensions();

        assertTrue(supportedExtensions.contains("png"));
    }

    @Test
    public void shouldReturnStartingBytesForJpegAndJpg() {
        byte[] expectedJpegBytes = {(byte) 0xff, (byte) 0xd8};
        byte[] startingBytes = SupportedImageExtension.JPEG.getStartingBytes();

        assertThat(startingBytes, is(expectedJpegBytes));
    }

    @Test
    public void shouldReturnStartingBytesForPng() {
        byte[] expectedPngBytes = {
                (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a
        };
        byte[] startingBytes = SupportedImageExtension.PNG.getStartingBytes();

        assertThat(startingBytes, is(expectedPngBytes));
    }
}