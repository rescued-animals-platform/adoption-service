package ec.animal.adoption.domain.media;

import org.junit.Test;

import java.util.Optional;

import static ec.animal.adoption.domain.media.SupportedImageExtension.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SupportedImageExtensionTest {

    @Test
    public void shouldReturnJpegFromExtensionAndContent() {
        byte[] jpegImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = SupportedImageExtension.getMatchFor(
                "jpeg", jpegImageContent
        );

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(JPEG));
    }

    @Test
    public void shouldReturnJpgFromExtensionAndContent() {
        byte[] jpgImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = SupportedImageExtension.getMatchFor(
                "jpg", jpgImageContent
        );

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(JPG));
    }

    @Test
    public void shouldReturnPngFromExtensionAndContent() {
        byte[] pngImageContent = {
                (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a
        };
        Optional<SupportedImageExtension> supportedImageExtension = SupportedImageExtension.getMatchFor(
                "png", pngImageContent
        );

        assertThat(supportedImageExtension.isPresent(), is(true));
        assertThat(supportedImageExtension.get(), is(PNG));
    }

    @Test
    public void shouldReturnEmptyFromUnmatchingExtension() {
        String unmatchingExtension = randomAlphabetic(10);
        byte[] jpgImageContent = {(byte) 0xff, (byte) 0xd8};
        Optional<SupportedImageExtension> supportedImageExtension = SupportedImageExtension.getMatchFor(
                unmatchingExtension, jpgImageContent
        );

        assertThat(supportedImageExtension.isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyFromUnmatchingContent() {
        byte[] unmatchingContent = {};
        Optional<SupportedImageExtension> supportedImageExtension = SupportedImageExtension.getMatchFor(
                "png", unmatchingContent
        );

        assertThat(supportedImageExtension.isPresent(), is(false));
    }
}